package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class NotFoundExceptionTest {

    @Test
    void messageConstructor() {
        String message = StringUtils.randomAlphaString(8);
        NotFoundException accessRestrictedException =
                assertThrows(NotFoundException.class,
                        () -> {
                            throw new NotFoundException(message);
                        }
                );

        assertEquals(message, accessRestrictedException.getMessage());
    }

    @Test
    void getCode() {
        final int exceptionCode = 404;
        NotFoundException exception = new NotFoundException(StringUtils.randomAlphaString(8));
        assertEquals(exceptionCode, exception.getCode());
    }
}