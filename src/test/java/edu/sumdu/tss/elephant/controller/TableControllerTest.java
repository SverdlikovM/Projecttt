package edu.sumdu.tss.elephant.controller;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
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

public class TableControllerTest {

    private static User user;
    private static Server server;
    private static WebDriver driver;
    private static Database DB;
    private static String sqlPath;
    private static String scriptPath;
    private static String tablePath;
    private static TablePage tablePage;
    private static SqlTerminalPage sqlTerminalPage;
    private static ScriptsPage scriptsPage;
    private static TablesListPage tablesListPage;

    @BeforeAll
    static void init() {
        Keys.loadParams(new File(Helper.configFile));
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        LoginPage loginPage = new LoginPage(driver);
        tablesListPage = new TablesListPage(driver);
        scriptsPage = new ScriptsPage(driver);
        tablePage = new TablePage(driver);
        sqlTerminalPage = new SqlTerminalPage(driver);
        sqlPath = Keys.get("APP.URL") + "/database/%s/sql/";
        tablePath = Keys.get("APP.URL") + "/database/%s/table/";
        scriptPath = Keys.get("APP.URL") + "/database/%s/script/";

        String password = Helper.genPassword();
        user = UserService.newDefaultUser();
        user.setLogin(Helper.genEmail());
        user.setPassword(password);
        user.setRole(UserRole.BASIC_USER.getValue());
        UserService.save(user);
        UserService.initUserStorage(user.getUsername());
        DbUserService.initUser(user.getUsername(), user.getPassword());

        String dbName = StringUtils.randomAlphaString(8);
        DB = new Database();
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

    @Test
    void initTableCheck() {
        String tableName = StringUtils.randomAlphaString(6);
        driver.get(String.format(sqlPath, DB.getName()));
        sqlTerminalPage.enterSqlCommand("create table "
                .concat(tableName).concat("(\n").concat("  name char,\n").concat("  value integer);"));
        sqlTerminalPage.enterSqlCommand("insert into ".concat(tableName)
                .concat(" values ('a',1);\n").concat("insert into ").concat(tableName).concat(" values ('b',2);"));
        sqlTerminalPage.enterSqlCommand("select * from ".concat(tableName).concat(";"));
        sqlTerminalPage.runButtonClick();

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initTableCheck_1");
        sqlTerminalPage.scroll();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initTableCheck_2");

        try {
            driver.get(String.format(tablePath, DB.getName()));
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initTableCheck_3");

            String table = tablesListPage.getTableName();
            tablesListPage.tableButtonClick();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initTableCheck_4");

            assertTrue(tablePage.getTableName().contains(table));
        } finally {
            driver.get(String.format(sqlPath, DB.getName()));
            sqlTerminalPage.enterSqlCommand("drop table ".concat(tableName).concat(";"));
            sqlTerminalPage.runButtonClick();
        }
    }

    @Test
    void initLargeTableCheck() {
        File file = new File("src/test/resources/check_2.sql");
        driver.get(String.format(scriptPath, DB.getName()));
        scriptsPage.loadScript(file);
        scriptsPage.uploadScript();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initLargeTableCheck_1");

        try {
            scriptsPage.runScript();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initLargeTableCheck_2");

            driver.get(String.format(tablePath, DB.getName()));
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initLargeTableCheck_3");

            driver.get(String.format(tablePath, DB.getName()).concat("large_table"));
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initLargeTableCheck_4");

            driver.manage().window().maximize();
            tablePage.goToNextPage();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "initLargeTableCheck_5");

            assertTrue(tablePage.getTableValueRight().contains("41"));
        } finally {
            driver.get(String.format(scriptPath, DB.getName()));
            scriptsPage.deleteScript();
        }
    }

    @AfterAll
    static void stop() {
        tablePage.logout();
        driver.quit();
        Helper.dropDB(DB);
        Helper.dropTablespace(user.getUsername());
        Helper.dropUser(user.getUsername());
        Helper.deleteUser(user.getLogin());
        server.stop();
    }
}