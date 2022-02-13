package edu.sumdu.tss.elephant.helper;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class HmacTest {

    @Test
    void calculate() throws NoSuchAlgorithmException, InvalidKeyException {
        Keys.loadParams(new File(Helper.configFile));

        String url = Keys.get("APP.URL");
        String key = StringUtils.randomAlphaString(20);
        String signature = Hmac.calculate(url, key);

        assertNotNull(signature);
    }
}