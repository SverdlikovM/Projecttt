package edu.sumdu.tss.elephant.helper.utils;

import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import io.javalin.http.util.ContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.sumdu.tss.elephant.helper.Keys;

public class ExceptionUtilsTest {

    @Test
    void stacktrace() {
        Exception exception = new Exception(StringUtils.randomAlphaString(8));
        String stacktrace = ExceptionUtils.stacktrace(exception);

        assertNotNull(stacktrace);
        assertFalse(stacktrace.isEmpty());
        assertTrue(stacktrace.contains(exception.getMessage()));
    }

    @Test
    void validationMessages() {
        final String errorMessage_1 = "Is it a valid mail?";
        Validator<String> validator = new Validator<>("userEmail", String.class, "login");

        try {
            validator.check(ValidatorHelper::isValidMail, errorMessage_1).get();
        } catch (Exception ex) {
            String errorMessage_2 = ExceptionUtils.validationMessages((io.javalin.core.validation.ValidationException) ex);

            assertNotNull(errorMessage_2);
            assertFalse(errorMessage_2.isEmpty());
            assertTrue(errorMessage_2.contains(errorMessage_1));
        }
    }

    @Test
    void wrapErrorPositiveValidation() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        HttpServletResponse response = mock(HttpServletResponse.class);
        Context context = spy(ContextUtil.init(request, response));
        Validator<String> validator = new Validator<>("userEmail", String.class, "login");

        try {
            validator.check(ValidatorHelper::isValidMail, "Is it a valid mail?").get();
        } catch (Exception ex) {
            ExceptionUtils.wrapError(context, ex);
            verify(context).sessionAttribute(eq(Keys.ERROR_KEY), anyString());
        }
    }

    @Test
    void wrapErrorNegativeValidation() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        HttpServletResponse response = mock(HttpServletResponse.class);
        Context context = spy(ContextUtil.init(request, response));
        String errorMessage = StringUtils.randomAlphaString(8);
        Exception exception = new Exception(errorMessage);
        ExceptionUtils.wrapError(context, exception);

        verify(context).sessionAttribute(eq(Keys.ERROR_KEY), eq(exception.getMessage()));
    }

    @Test
    void isSQLUniqueException() {
        Exception exception_1 = new Exception("duplicate key value violates unique constraint");
        Exception exception_2 = new Exception();

        assertTrue(ExceptionUtils.isSQLUniqueException(exception_1));
        assertFalse(ExceptionUtils.isSQLUniqueException(exception_2));
    }
}