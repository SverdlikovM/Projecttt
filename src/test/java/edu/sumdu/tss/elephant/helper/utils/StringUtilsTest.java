package edu.sumdu.tss.elephant.helper.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    void randomAlphaString() {
        String string_1 = StringUtils.randomAlphaString(10);
        String string_2 = StringUtils.randomAlphaString(10);

        assertNotEquals(string_1, string_2);
    }

    @Test
    void uuid() {
        String string_1 = StringUtils.uuid();
        String string_2 = StringUtils.uuid();

        assertNotEquals(string_1, string_2);
    }

    @Test
    void replaceLast() {
        String str_1 = StringUtils.randomAlphaString(15);
        str_1 = str_1.concat(str_1);

        String substr_1 = str_1.substring(7, 12);
        String substr_2 = StringUtils.randomAlphaString(5);
        String str_2 = StringUtils.replaceLast(str_1, substr_1, substr_2);

        assertNotEquals(str_1, str_2);
        assertEquals(substr_2, str_2.substring(22, 27));
        assertNotEquals(substr_2, str_2.substring(7, 12));

        String str3 = StringUtils.replaceLast(str_1, substr_2, substr_1);
        assertEquals(str_1, str3);
    }
}