package edu.sumdu.tss.elephant.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class PairTest {

    @Test
    void pair() {
        String key = StringUtils.randomAlphaString(7);
        String value = StringUtils.randomAlphaString(13);
        Pair<String, String> pair_1 = new Pair<>(key, value);

        assertEquals(key, pair_1.getKey());
        assertEquals(value, pair_1.getValue());

        Pair<String, String> pair_2 = new Pair<>();
        assertNotNull(pair_2);
    }
}