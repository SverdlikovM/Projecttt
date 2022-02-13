package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class AccessRestrictedExceptionTest {

    @Test
    void emptyConstructor() {
        assertThrows(AccessRestrictedException.class,
                () -> {
                    throw new AccessRestrictedException();
                }
        );
    }

    @Test
    void exceptionConstructor() {
        Exception exception = new Exception(StringUtils.randomAlphaString(8));
        AccessRestrictedException accessRestrictedException =
                assertThrows(AccessRestrictedException.class,
                        () -> {
                            throw new AccessRestrictedException(exception);
                        }
                );

        assertTrue(accessRestrictedException.getMessage().contains(exception.getMessage()));
        assertEquals(exception, accessRestrictedException.getCause());
    }

    @Test
    void messageConstructor() {
        String message = StringUtils.randomAlphaString(8);
        AccessRestrictedException accessRestrictedException =
                assertThrows(AccessRestrictedException.class,
                        () -> {
                            throw new AccessRestrictedException(message);
                        }
                );

        assertEquals(message, accessRestrictedException.getMessage());
    }

    @Test
    void getCode() {
        final int exceptionCode = 400;
        AccessRestrictedException exception = new AccessRestrictedException();
        assertEquals(exceptionCode, exception.getCode());
    }
}