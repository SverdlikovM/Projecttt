package edu.sumdu.tss.elephant.helper;

import java.io.*;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class KeysTest {

    static File file;

    @BeforeAll
    static void file() {
        file = new File(Helper.configFile);
    }

    @Test
    void get() {
        Keys.loadParams(file);

        assertThrows(RuntimeException.class, () -> Keys.get(StringUtils.randomAlphaString(6)));

        String db = Keys.get("DB.NAME");

        assertNotNull(db);
        assertFalse(db.isEmpty());
    }

    @Test
    void loadWrongFile() {
        assertThrows(RuntimeException.class, () -> Keys.loadParams(new File("")));
    }

    @Test
    void negativeLoadParam() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            properties.remove("DB.NAME");

            File testFile = new File("negativeLoadParam.properties");
            FileOutputStream fo = new FileOutputStream(testFile);
            properties.store(fo, "Properties");
            fo.close();

            assertThrows(IllegalArgumentException.class, () -> Keys.loadParams(testFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void negativeLoadSecureParam() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            properties.remove("DB.PASSWORD");

            File testFile = new File("negativeLoadSecureParam.properties");
            FileOutputStream fo = new FileOutputStream(testFile);
            properties.store(fo, "Properties");
            fo.close();

            assertThrows(IllegalArgumentException.class, () -> Keys.loadParams(testFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isProduction() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(file));

            assertEquals(properties.getProperty("ENV")
                            .equalsIgnoreCase("production"),
                    Keys.isProduction()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}