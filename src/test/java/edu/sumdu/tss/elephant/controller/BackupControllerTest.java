package edu.sumdu.tss.elephant.controller;

import java.io.File;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.Server;
import edu.sumdu.tss.elephant.model.*;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import edu.sumdu.tss.elephant.helper.exception.NotFoundException;
import edu.sumdu.tss.elephant.containers.LoginPage;
import edu.sumdu.tss.elephant.containers.BackupPage;

public class BackupControllerTest {

    private static User user;
    private static Server server;
    private static WebDriver driver;
    private static Database DB;
    private static String path;
    private static BackupPage backupPage;

    @BeforeAll
    static void init() {
        Keys.loadParams(new File(Helper.configFile));
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        backupPage = new BackupPage(driver);
        path = Keys.get("APP.URL") + "/DB/%s/point/";

        LoginPage loginPage = new LoginPage(driver);
        String password = Helper.genPassword();
        user = UserService.newDefaultUser();
        user.setLogin(Helper.genEmail());
        user.setPassword(password);
        user.setRole(UserRole.BASIC_USER.getValue());
        UserService.save(user);
        UserService.initUserStorage(user.getUsername());
        DbUserService.initUser(user.getUsername(), user.getPassword());

        String dbName = StringUtils.randomAlphaString(6);
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

    @BeforeEach
    void refresh() {
        driver.get(String.format(path, DB.getName()));
    }

    @Test
    void createBackupCheck() {
        final String expectedStatus = "Backup created successfully";

        String pointName = StringUtils.randomAlphaString(6);
        backupPage.enterName(pointName);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "createBackupCheck_1");
        backupPage.create_1();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "createBackupCheck_2");

        assertEquals(expectedStatus, backupPage.getAlertMessage());
        assertTrue(backupPage.getPointName().contains(pointName));

        Backup backup = BackupService.byName(DB.getName(), pointName);
        assertNotNull(backup);

        backupPage.delete_1();
    }

    @Test
    void createNoNameBackupCheck() {
        final String expectedStatus = "Point name can't be empty";

        backupPage.create_1();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "createNoNameBackupCheck");

        assertEquals(expectedStatus, backupPage.getAlertMessage());
    }

    @Test
    void createBackupNegativeCheck() {
        final String expectedStatus = "You limit reached";

        String pointName_1 = StringUtils.randomAlphaString(6);
        backupPage.enterName(pointName_1);
        backupPage.create_1();

        String pointName_2 = StringUtils.randomAlphaString(6);
        backupPage.enterName(pointName_2);
        backupPage.create_2();

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "createBackupNegativeCheck");
        assertEquals(expectedStatus, backupPage.getAlertMessage());

        backupPage.delete_1();
    }

    @Test
    void updateBackupCheck() {
        final String expectedStatus = "Backup created successfully";

        String pointName = StringUtils.randomAlphaString(6);
        backupPage.enterName(pointName);
        backupPage.create_1();
        Backup backup_1 = BackupService.byName(DB.getName(), pointName);
        backupPage.update_1();

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "updateBackupCheck");
        assertEquals(expectedStatus, backupPage.getAlertMessage());

        Backup backup_2 = BackupService.byName(DB.getName(), pointName);
        assertNotEquals(backup_1, backup_2);

        backupPage.delete_1();
    }

    @Test
    void deleteBackupCheck() {
        String pointName = StringUtils.randomAlphaString(6);
        backupPage.enterName(pointName);
        backupPage.create_1();

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "deleteBackupCheck_1");
        Backup backup = BackupService.byName(DB.getName(), pointName);
        assertNotNull(backup);
        backupPage.delete_1();

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "deleteBackupCheck_2");
        assertThrows(NotFoundException.class, () -> BackupService.byName(DB.getName(), pointName));
    }

    @Test
    void restoreDBCheck() {
        final String expectedStatus = "Restore performed successfully";

        String pointName = StringUtils.randomAlphaString(6);
        backupPage.enterName(pointName);
        backupPage.create_1();
        backupPage.reset_1();

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "restoreDBCheck");
        assertEquals(expectedStatus, backupPage.getAlertMessage());

        backupPage.delete_1();
    }

    @AfterAll
    static void stop() {
        backupPage.logout();
        driver.quit();
        Helper.dropDB(DB);
        Helper.dropTablespace(user.getUsername());
        Helper.dropUser(user.getUsername());
        Helper.deleteUser(user.getLogin());
        server.stop();
    }
}