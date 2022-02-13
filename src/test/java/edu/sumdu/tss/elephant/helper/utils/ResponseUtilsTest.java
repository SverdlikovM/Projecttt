package edu.sumdu.tss.elephant.helper.utils;

import io.javalin.http.Context;
import io.javalin.http.util.ContextUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import edu.sumdu.tss.elephant.helper.ViewHelper;

public class ResponseUtilsTest {

    @Test
    void success() {
        String responseMessage = "User validated successfully.";
        HashMap<String, String> response = (HashMap<String, String>) ResponseUtils.success(responseMessage);
        assertEquals(responseMessage, response.get("message"));

        String responseStatus = "ok";
        assertTrue(responseStatus.equalsIgnoreCase(response.get("status")));
    }

    @Test
    void error() {
        String responseMessage = "Can't validate user.";
        HashMap<String, String> response = (HashMap<String, String>) ResponseUtils.error(responseMessage);
        assertEquals(responseMessage, response.get("message"));

        String responseStatus = "error";
        assertTrue(responseStatus.equalsIgnoreCase(response.get("status")));
    }

    @Test
    void flush_flash() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(servletRequest.getSession()).thenReturn(session);

        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
        Context context = spy(ContextUtil.init(servletRequest, servletResponse));
        ResponseUtils.flush_flash(context);

        for (var key : ViewHelper.FLASH_KEY) {
            verify(context).sessionAttribute(eq(key), eq(null));
        }
    }
}