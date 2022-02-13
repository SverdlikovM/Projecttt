package edu.sumdu.tss.elephant.controller;

import java.io.File;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.Server;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.enums.Lang;
import edu.sumdu.tss.elephant.helper.utils.MessageBundle;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import edu.sumdu.tss.elephant.model.*;
import edu.sumdu.tss.elephant.containers.LoginPage;
import edu.sumdu.tss.elephant.containers.ProfilePage;

public class ProfileControllerTest {

    private static User user;
    private static Server server;
    private static WebDriver driver;
    private static String path;
    private static ProfilePage profilePage;

    @BeforeAll
    static void init() {
        Keys.loadParams(new File(Helper.configFile));
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        profilePage = new ProfilePage(driver);
        path = Keys.get("APP.URL") + "/profile";

        LoginPage loginPage = new LoginPage(driver);
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

        loginPage.enterUsername(user.getLogin());
        loginPage.enterPassword(password);
        loginPage.clickLogInButton();
    }

    @BeforeEach
    void refresh() {
        driver.get(path);
    }

    @Test
    void resetDBPasswordCheck() {
        String newDbPassword = StringUtils.randomAlphaString(8);
        profilePage.scrollToDB();
        profilePage.enterDBPassword(newDbPassword);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetDBPasswordCheck_1");
        profilePage.changeDBPasswordClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetDBPasswordCheck_2");

        String successMessage = profilePage.getAlertMessage();
        assertEquals("DB user password was changed", successMessage);

        user = UserService.byLogin(user.getLogin());
        assertEquals(newDbPassword, user.getDbPassword());
    }

    @Test
    void resetWebPasswordCheck() {
        String newPassword = Helper.genPassword();
        profilePage.scrollToWebUser();
        profilePage.enterPassword(newPassword);
        profilePage.enterConfirmPassword(newPassword);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetWebPasswordCheck_1");
        profilePage.changePasswordClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetWebPasswordCheck_2");

        String successMessagfe = profilePage.getAlertMessage();
        assertEquals("Web user password was changed", successMessagfe);

        user = UserService.byLogin(user.getLogin());
        assertEquals(user.crypt(newPassword), user.getPassword());
    }

    @Test
    void resetApiKeysCheck() {
        profilePage.scrollToRegenerate_1();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetApiKeysCheck_1");
        profilePage.regenerateClick_1();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetApiKeysCheck_2");

        String successMessage = profilePage.getAlertMessage();
        assertEquals("API keys was reset successful", successMessage);

        String newPublicKey = profilePage.getPublicApiKey();
        assertNotEquals(user.getPublicKey(), newPublicKey);

        String newPrivateKey = profilePage.getPrivateApiKey();
        assertNotEquals(user.getPrivateKey(), newPrivateKey);

        user = UserService.byLogin(user.getLogin());
        assertEquals(newPublicKey, user.getPublicKey());
        assertEquals(newPrivateKey, user.getPrivateKey());
    }

    @Test
    void upgradeUserCheck() {
        profilePage.scrollToWebUser();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "upgradeUserCheck_1");
        profilePage.proUpgradeClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "upgradeUserCheck_2");

        String successMessage = profilePage.getAlertMessage();
        assertEquals("Role has been changed", successMessage);

        user = UserService.byLogin(user.getLogin());
        assertEquals(UserRole.PROMOTED_USER, user.role());

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "upgradeUserCheck_3");
        profilePage.scrollToUpgrade();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "upgradeUserCheck_4");
        profilePage.basicUpgradeClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "upgradeUserCheck_5");

        successMessage = profilePage.getAlertMessage();
        assertEquals("Role has been changed", successMessage);

        user = UserService.byLogin(user.getLogin());
        assertEquals(UserRole.BASIC_USER, user.role());
    }

    @Test
    void index() {
        String DBUsername = profilePage.getDBUsername();
        assertEquals(user.getUsername(), DBUsername);

        String DBPassword = profilePage.getDBPassword();
        assertEquals(user.getDbPassword(), DBPassword);

        String publicKey = profilePage.getPublicApiKey();
        assertEquals(user.getPublicKey(), publicKey);

        String privateKey = profilePage.getPrivateApiKey();
        assertEquals(user.getPrivateKey(), privateKey);
    }

    @Test
    void language() {
        MessageBundle messageBundle;

        profilePage.languageEN_1_click();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "language_" + Lang.EN);
        messageBundle = new MessageBundle(Lang.EN.toString());
        assertEquals(messageBundle.get("controls.button.logout"), profilePage.getLogoutText());

        profilePage.languageUKClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "language_" + Lang.UK);
        messageBundle = new MessageBundle(Lang.UK.toString());
        assertEquals(messageBundle.get("controls.button.logout"), profilePage.getLogoutText());

        driver.get(path + "/lang?lang=" + Keys.get("DEFAULT_LANG").toLowerCase(Locale.ROOT));
    }

    @AfterAll
    static void stop() {
        driver.quit();
        Helper.dropTablespace(user.getUsername());
        Helper.dropUser(user.getUsername());
        Helper.deleteUser(user.getLogin());
        server.stop();
    }
}