package edu.sumdu.tss.elephant.helper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class DBPoolTest {

    private static final String SIMPLE_GET = "SELECT 0";

    @BeforeAll
    static void loadProps() {
        Keys.loadParams(new File(Helper.configFile));
    }

    @Test
    void getConnection() {
        Connection connection = DBPool.getConnection().open();
        int selection = connection.createQuery(SIMPLE_GET).executeScalar(Integer.class);

        assertEquals(0, selection);
        connection.close();

        assertThrows(Sql2oException.class, () -> DBPool.getConnection(StringUtils.randomAlphaString(6)).open());
    }

    @Test
    void dbUtilUrl() {
        String name = StringUtils.randomAlphaString(6);
        String url_1 = String.format("postgresql://%s:%s@%s:%s/%s",
                Keys.get("DB.USERNAME"),
                Keys.get("DB.PASSWORD"),
                Keys.get("DB.URL"),
                Keys.get("DB.PORT"),
                name
        );
        String url_2 = DBPool.dbUtilUrl(name);

        assertEquals(url_1, url_2);
    }
}