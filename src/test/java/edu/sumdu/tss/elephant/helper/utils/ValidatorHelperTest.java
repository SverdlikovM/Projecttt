package edu.sumdu.tss.elephant.helper.utils;

import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorHelperTest {

    @Test
    void isValidPassword() {
        String invalidFormat = StringUtils.randomAlphaString(12);
        String validFormat = StringUtils.randomAlphaString(4).toUpperCase()
                .concat(",")
                .concat(StringUtils.randomAlphaString(2))
                + new Random().nextInt();

        assertFalse(ValidatorHelper.isValidPassword(invalidFormat));
        assertTrue(ValidatorHelper.isValidPassword(validFormat));
    }

    @Test
    void isValidEmail() {
        String invalidFormat = StringUtils.randomAlphaString(12);
        String validFormat = StringUtils.randomAlphaString(3)
                .concat("@")
                .concat(StringUtils.randomAlphaString(3))
                .concat(".")
                .concat(StringUtils.randomAlphaString(3));

        assertFalse(ValidatorHelper.isValidMail(invalidFormat));
        assertTrue(ValidatorHelper.isValidMail(validFormat));
    }
}