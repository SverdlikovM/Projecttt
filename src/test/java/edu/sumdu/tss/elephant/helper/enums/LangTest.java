package edu.sumdu.tss.elephant.helper.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class LangTest {

    @Test
    void byValue() {
        Lang checkEn = Lang.byValue("EN");
        assertEquals("EN", checkEn.toString());

        Lang checkUk = Lang.byValue("UK");
        assertEquals("UK", checkUk.toString());

        assertThrows(RuntimeException.class, () -> Lang.byValue(StringUtils.randomAlphaString(2)));
    }
}