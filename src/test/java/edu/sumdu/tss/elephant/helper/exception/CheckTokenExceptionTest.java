package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckTokenExceptionTest {

    @Test
    void emptyConstructor() {
        assertThrows(CheckTokenException.class,
                () -> {
                    throw new CheckTokenException();
                }
        );
    }
}
