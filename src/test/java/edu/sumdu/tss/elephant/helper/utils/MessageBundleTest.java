package edu.sumdu.tss.elephant.helper.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageBundleTest {

    @Test
    void positiveTest() {
        MessageBundle bundle = new MessageBundle("EN");
        String message = bundle.get("validation.mail.empty");

        assertNotNull(message);
        assertFalse(message.isEmpty());

        assertFalse(message.startsWith("I18n not found:"));
    }

    @Test
    void negativeTest() {
        MessageBundle bundle = new MessageBundle("EN");
        String message = bundle.get(StringUtils.randomAlphaString(15));

        assertNotNull(message);
        assertFalse(message.isEmpty());

        assertTrue(message.startsWith("I18n not found:"));
    }
}