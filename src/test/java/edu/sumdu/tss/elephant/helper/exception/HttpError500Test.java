package edu.sumdu.tss.elephant.helper.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpError500Test {

    @Test
    void emptyConstructor() {
        assertThrows(HttpError500.class,
                () -> {
                    throw new HttpError500();
                }
        );
    }

    @Test
    void getIcon() {
        final String expected = "bug";
        HttpError500 error = new HttpError500();
        assertEquals(expected, error.getIcon());
    }
}