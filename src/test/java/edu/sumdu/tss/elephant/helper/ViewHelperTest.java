package edu.sumdu.tss.elephant.helper;

import io.javalin.http.Context;
import io.javalin.http.util.ContextUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.helper.exception.HttpError400;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import edu.sumdu.tss.elephant.model.Database;
import edu.sumdu.tss.elephant.model.DatabaseService;
import edu.sumdu.tss.elephant.model.User;
import edu.sumdu.tss.elephant.model.UserService;

public class ViewHelperTest {

    @BeforeAll
    static void init() {
        Keys.loadParams(new File(Helper.configFile));
    }

    @Test
    void pager() {
        Random rndm = new Random();

        int total = rndm.nextInt(50) + 1;
        int current = rndm.nextInt(total) + 1;
        String pages = ViewHelper.pager(total + 1, current);

        assertNotNull(pages);
        assertFalse(pages.isEmpty());
        for (int i = 1; i <= total; i++) {
            assertTrue(pages.contains(String.valueOf(i)));
        }
    }

    @Test
    void breadcrumb() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        HttpServletResponse response = mock(HttpServletResponse.class);
        Context context = spy(ContextUtil.init(request, response));

        when(context.sessionAttribute(Keys.BREADCRUMB_KEY)).thenReturn(null);

        List<String> bCrumb = ViewHelper.breadcrumb(context);

        assertNotNull(bCrumb);
        verify(context).sessionAttribute(eq(Keys.BREADCRUMB_KEY), eq(bCrumb));
    }

    @Test
    void cleanupSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        HttpServletResponse response = mock(HttpServletResponse.class);
        Context context = spy(ContextUtil.init(request, response));

        doNothing().when(context).sessionAttribute(anyString(), anyString());
        ViewHelper.cleanupSession(context);

        verify(context).sessionAttribute(eq(Keys.MODEL_KEY), eq(null));
        verify(context).sessionAttribute(eq(Keys.DB_KEY), eq(null));
        verify(context).sessionAttribute(eq(Keys.BREADCRUMB_KEY), eq(null));
    }

    @Test
    void defaultVariables() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestURI()).thenReturn("/home");

        HttpServletResponse response = mock(HttpServletResponse.class);
        Context context = spy(ContextUtil.init(request, response));
        User user = UserService.newDefaultUser();

        when(context.sessionAttribute(Keys.SESSION_CURRENT_USER_KEY)).thenReturn(user);
        when(context.sessionAttribute(Keys.LANG_KEY)).thenReturn(Keys.get("DEFAULT_LANG"));
        when(context.sessionAttribute(Keys.BREADCRUMB_KEY)).thenReturn(null);
        when(context.path()).thenReturn("database/test");
        when(context.sessionAttribute(Keys.INFO_KEY)).thenReturn("test");

        try (MockedStatic<DatabaseService> mocked = mockStatic(DatabaseService.class)) {
            Database db = new Database();
            mocked.when(() -> DatabaseService.activeDatabase(anyString(), anyString())).thenReturn(db);
            ViewHelper.defaultVariables(context);

            verify(context).sessionAttribute(eq(Keys.MODEL_KEY), anyMap());
            verify(context).sessionAttribute(eq(Keys.DB_KEY), eq(db));
            verify(context).sessionAttribute(eq(Keys.INFO_KEY), eq(null));
        }
    }

    @Test
    void userErrorTest() {
        final int errorStatus = 400;

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(Keys.MODEL_KEY)).thenReturn(new HashMap<>());

        HttpServletResponse response = mock(HttpServletResponse.class);
        Context context = spy(ContextUtil.init(request, response));
        ViewHelper.userError(new HttpError400(), context);

        verify(context).status(errorStatus);
        verify(context).render(eq("/velocity/error.vm"), anyMap());
    }

    @Test
    void softError() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Context context = spy(ContextUtil.init(request, response));

        doNothing().when(context).sessionAttribute(anyString(), anyString());
        doNothing().when(context).redirect(anyString());
        doReturn(null).when(context).header("Referer");

        String errorMessage = StringUtils.randomAlphaString(16);
        ViewHelper.softError(errorMessage, context);

        verify(context).sessionAttribute(eq(Keys.ERROR_KEY), eq(errorMessage));
        verify(context).redirect(eq("/"));
    }
}