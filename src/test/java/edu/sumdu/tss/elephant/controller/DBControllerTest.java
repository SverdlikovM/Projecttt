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

import static org.junit.jupiter.api.Assertions.*;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.Server;
import edu.sumdu.tss.elephant.model.*;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.utils.MessageBundle;
import edu.sumdu.tss.elephant.containers.*;

public class DBControllerTest {

    private static User user;
    private static Server server;
    private static WebDriver driver;
    private static String path;
    private static HomePage homePage;

    @BeforeAll
    static void init() {
        Keys.loadParams(new File(Helper.configFile));
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        LoginPage loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        String password = Helper.genPassword();
        user = UserService.newDefaultUser();
        user.setLogin(Helper.genEmail());
        user.setPassword(password);
        user.setRole(UserRole.BASIC_USER.getValue());
        UserService.save(user);
        UserService.initUserStorage(user.getUsername());
        DbUserService.initUser(user.getUsername(), user.getPassword());

        server = new Server();
        server.start(Integer.parseInt(Keys.get("APP.PORT")));
        driver.get(Keys.get("APP.URL") + "/login");
        path = Keys.get("APP.URL") + "/home";

        loginPage.enterUsername(user.getLogin());
        loginPage.enterPassword(password);
        loginPage.clickLogInButton();
    }

    @BeforeEach
    void refresh() {
        driver.get(path);
    }

    @Test
    void createDBCheck() {
        final String expectedStatus = "Database created";

        homePage.createDB_1();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "createDBCheck");

        DBPage dbPage = new DBPage(driver);
        try {
            assertEquals(expectedStatus, dbPage.getAlertMessage());
            String dbName = dbPage.getDBName();

            Database database = DatabaseService.byName(dbName);
            assertEquals(user.getUsername(), database.getOwner());
        } finally {
            driver.get(path);
            homePage.deleteDB();
        }
    }

    @Test
    void createMaxDBsCheck() {
        for (int i = 1; i <= user.role().maxDB(); i++) {
            homePage.createDB_1();
            driver.get(path);
        }

        try {
            homePage.scrollToCreateButton();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "createMaxDBsCheck");

            String message = homePage.fullDBPlanMessage();
            assertTrue(message.contains(new MessageBundle(Keys.get("DEFAULT_LANG")).get("database.form.limits")));
        } finally {
            for (int i = 1; i <= user.role().maxDB(); i++) {
                homePage.deleteDB();
                driver.get(path);
            }
        }
    }

    @Test
    void deleteDBCheck() {
        homePage.createDB_1();
        driver.get(path);

        homePage.deleteDB();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "deleteDBCheck");
        assertTrue(homePage.getAlertMessage().contains("Database has been dropped"));

        List<Database> list = DatabaseService.forUser(user.getUsername());
        assertTrue(list.isEmpty());
    }

    @AfterAll
    static void stop() {
        homePage.logout();
        driver.quit();
        Helper.dropTablespace(user.getUsername());
        Helper.dropUser(user.getUsername());
        Helper.deleteUser(user.getLogin());
        server.stop();
    }
}