package edu.sumdu.tss.elephant;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.sql2o.Connection;

import edu.sumdu.tss.elephant.helper.DBPool;
import edu.sumdu.tss.elephant.helper.utils.ParameterizedStringFactory;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import edu.sumdu.tss.elephant.model.Database;
import edu.sumdu.tss.elephant.model.DatabaseService;
import edu.sumdu.tss.elephant.model.DbUserService;

public class Helper {

    public static final String configFile = "config.properties";
    private static final ParameterizedStringFactory DROP_TABLESPACE_SQL =
            new ParameterizedStringFactory("DROP TABLESPACE :name");
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE login = :login";

    public static void dropDB(Database DB) {
        try {
            DatabaseService.drop(DB);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dropTablespace(String tableSpaceName) {
        try (Connection connection = DBPool.getConnection().open()) {
            connection.createQuery(DROP_TABLESPACE_SQL.addParameter("name", tableSpaceName).toString(),
                    false).executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dropUser(String userName) {
        try {
            DbUserService.dropUser(userName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUser(String email) {
        try (Connection connection = DBPool.getConnection().open()) {
            connection.createQuery(DELETE_USER_SQL).addParameter("login", email).executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void recursiveDelete(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }

        if (file.delete()) {
            System.out.println("File or folder by following path was removed: " + file.getAbsolutePath());
        } else {
            System.out.println("Error: can't remove the following file or folder:" + file.getAbsolutePath());
        }
    }

    public static String genEmail() {
        return StringUtils.randomAlphaString(3)
                .concat("@")
                .concat(StringUtils.randomAlphaString(3))
                .concat(".")
                .concat(StringUtils.randomAlphaString(3));
    }

    public static String genPassword() {
        return StringUtils.randomAlphaString(4).toUpperCase()
                .concat(",")
                .concat(StringUtils.randomAlphaString(2))
                + new Random().nextInt();
    }

    public static void takeScreenshot(WebDriver driver, String directory, String fileName) {
        File resource = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss.SSSS");
        String path = "src/test/screenshots/" + directory + "/"
                + LocalDateTime.now().format(format) + " " + fileName + ".png";

        try {
            FileUtils.copyFile(resource, new File(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}