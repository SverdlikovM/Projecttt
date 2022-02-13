package edu.sumdu.tss.elephant.controller;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.Server;
import edu.sumdu.tss.elephant.model.*;
import edu.sumdu.tss.elephant.containers.*;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;

public class ScriptsControllerTest {

    private static User user;
    private static Server server;
    private static WebDriver driver;
    private static String path;
    private static Database DB;
    private static ScriptsPage scriptsPage;

    @BeforeAll
    static void init() {
        Keys.loadParams(new File(Helper.configFile));
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        scriptsPage = new ScriptsPage(driver);
        path = Keys.get("APP.URL") + "/DB/%s/script/";

        LoginPage loginPage = new LoginPage(driver);
        String password = Helper.genPassword();
        user = UserService.newDefaultUser();
        user.setLogin(Helper.genEmail());
        user.setPassword(password);
        user.setRole(UserRole.BASIC_USER.getValue());
        UserService.save(user);
        UserService.initUserStorage(user.getUsername());
        DbUserService.initUser(user.getUsername(), user.getPassword());

        DB = new Database();
        String dbName = StringUtils.randomAlphaString(8);
        DB.setName(dbName);
        DB.setOwner(user.getUsername());
        DatabaseService.create(dbName, user.getUsername(), user.getUsername());

        server = new Server();
        server.start(Integer.parseInt(Keys.get("APP.PORT")));
        driver.get(Keys.get("APP.URL") + "/login");

        loginPage.enterUsername(user.getLogin());
        loginPage.enterPassword(password);
        loginPage.clickLogInButton();
    }

    @BeforeEach
    void refreshPage() {
        driver.get(String.format(path, DB.getName()));
    }

    @Test
    void loadScriptTCheck() {
        File file = new File("src/test/resources/check_1.sql");

        scriptsPage.loadScript(file);
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "loadScriptTCheck_1");
        scriptsPage.uploadScript();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "loadScriptTCheck_2");

        try {
            assertTrue(scriptsPage.getScriptName().contains("check_1.sql"));
        } finally {
            scriptsPage.deleteScript();
        }
    }

    @Test
    void loadEmptyNegativeCheck() {
        final String expectedStatus = "no file";

        scriptsPage.uploadScript();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "uploadEmptyNegativeCheck");

        try {
            assertTrue(scriptsPage.getAlertMessage().contains(expectedStatus));
        } finally {
            driver.get(String.format(path, DB.getName()));
            scriptsPage.deleteScript();
        }
    }

    @Test
    void loadScriptNegativeCheck() {
        File file = new File("src/test/resources/check_1.sql");
        scriptsPage.loadScript(file);
        scriptsPage.uploadScript();
        scriptsPage.loadScript(file);
        scriptsPage.uploadScript();
        scriptsPage.loadScript(file);
        scriptsPage.uploadScript();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "loadScriptNegativeCheck");

        try {
            assertTrue(scriptsPage.getAlertMessage().contains("limit") && scriptsPage.getAlertMessage().contains("reached"));
        } finally {
            driver.get(String.format(path, DB.getName()));
            scriptsPage.deleteScript();
            scriptsPage.deleteScript();
        }
    }

    @Test
    void runLargeScriptCheck() {
        File file = new File("src/test/resources/check_2.sql");
        scriptsPage.loadScript(file);
        scriptsPage.uploadScript();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "runLargeScriptCheck_1");

        try {
            scriptsPage.runScript();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "runLargeScriptCheck_2");

            TablesListPage tablesListPage = new TablesListPage(driver);
            String pathToTables = Keys.get("APP.URL") + "/DB/%s/table/";
            driver.get(String.format(pathToTables, DB.getName()));
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "runLargeScriptCheck_3");

            String tableName = tablesListPage.getTableName();
            System.out.println(tableName);
            TablePage tablePage = new TablePage(driver);
            String pathToTable = Keys.get("APP.URL") + "/DB/%s/table/" + tableName + "/";
            driver.get(String.format(pathToTable, DB.getName()));

            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "runLargeScriptCheck_4");
            assertTrue(tablePage.getTableValueLeft().contains("a"));
        } finally {
            driver.get(String.format(path, DB.getName()));
            scriptsPage.deleteScript();
        }
    }

    @Test
    void deleteScriptCheck() {
        File file = new File("src/test/resources/check_1.sql");
        scriptsPage.loadScript(file);
        scriptsPage.uploadScript();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "deleteScriptCheck_1");
        scriptsPage.deleteScript();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "deleteScriptCheck_2");

        List<Script> list = ScriptService.list(DB.getName());
        assertTrue(list.isEmpty());
    }

    @AfterAll
    static void stop() {
        scriptsPage.logout();
        driver.quit();
        Helper.dropDB(DB);
        Helper.dropTablespace(user.getUsername());
        Helper.dropUser(user.getUsername());
        Helper.deleteUser(user.getLogin());
        server.stop();
    }
}