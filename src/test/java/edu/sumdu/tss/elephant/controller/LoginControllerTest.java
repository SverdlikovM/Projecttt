package edu.sumdu.tss.elephant.controller;

import java.io.File;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.Server;
import edu.sumdu.tss.elephant.model.User;
import edu.sumdu.tss.elephant.model.UserService;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.enums.Lang;
import edu.sumdu.tss.elephant.helper.utils.MessageBundle;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import edu.sumdu.tss.elephant.containers.HomePage;
import edu.sumdu.tss.elephant.containers.LoginPage;
import edu.sumdu.tss.elephant.containers.ResetPasswordPage;
import edu.sumdu.tss.elephant.containers.ResetPage;

public class LoginControllerTest {

    private static Server server;
    private static WebDriver driver;
    private static MessageBundle mb;
    private static String path;
    private static LoginPage loginPage;
    private static HomePage homePage;
    private static String password;

    @BeforeAll
    static void init() {
        Keys.loadParams(new File(Helper.configFile));
        WebDriverManager.chromedriver().setup();

        password = Helper.genPassword();
        server = new Server();
        server.start(Integer.parseInt(Keys.get("APP.PORT")));
        driver = new ChromeDriver();

        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        path = Keys.get("APP.URL") + "/login";
        mb = new MessageBundle(Keys.get("DEFAULT_LANG"));
    }

    @Test
    void logInCheck() {
        driver.get(path);

        User user = UserService.newDefaultUser();
        String email = Helper.genEmail();
        user.setLogin(email);
        user.setPassword(password);
        UserService.save(user);
        loginPage.enterUsername(email);
        loginPage.enterPassword(password);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "logInCheck_1");
        loginPage.clickLogInButton();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "logInCheck_2");

        try {
            assertEquals(email, homePage.getCurrentUsername());
        } finally {
            homePage.logout();
            Helper.deleteUser(user.getLogin());
        }
    }

    @Test
    void noEmailCheck() {
        driver.get(path);

        loginPage.enterPassword(password);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "noEmailCheck_1");
        loginPage.clickLogInButton();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "noEmailCheck_2");

        String errorMessage = loginPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.email.empty")));
    }

    @Test
    void noPasswordCheck() {
        driver.get(path);
        String email = Helper.genEmail();
        loginPage.enterUsername(email);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "noPasswordCheck_1");
        loginPage.clickLogInButton();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "noPasswordCheck_2");

        String errorMessage = loginPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.password.empty")));
    }

    @Test
    void wrongEmailCheck() {
        driver.get(path);

        String email = Helper.genEmail();
        loginPage.enterUsername(email);
        loginPage.enterPassword(password);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "wrongEmailCheck_1");
        loginPage.clickLogInButton();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "wrongEmailCheck_2");

        String errorMessage = loginPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.user.not_found")));
    }

    @Test
    void wrongPasswordCheck() {
        driver.get(path);

        User user = UserService.newDefaultUser();
        String email = Helper.genEmail();
        user.setLogin(email);
        user.setPassword(password);
        UserService.save(user);

        loginPage.enterUsername(email);
        loginPage.enterPassword(Helper.genPassword());

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "wrongPasswordCheck_1");
        loginPage.clickLogInButton();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "wrongPasswordCheck_2");

        String errorMessage = "";
        try {
            errorMessage = loginPage.getAlertMessage();
        } catch (Exception e) {
            e.printStackTrace();
            homePage.logout();
        }
        assertTrue(errorMessage.contains(mb.get("validation.user.not_found")));
    }

    @Test
    void resetLinkCheck() {
        driver.get(path);

        User user = UserService.newDefaultUser();
        String email = Keys.get("EMAIL.FROM");
        user.setLogin(email);
        user.setPassword(password);
        UserService.save(user);

        try {
            loginPage.clickForgotPasswordLink();
            ResetPage resetPage = new ResetPage(driver);
            resetPage.enterUsername(email);

            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetLinkCheck_1");
            resetPage.clickSendButton();
            String successMessage = loginPage.getAlertMessage();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetLinkCheck_2");

            assertTrue(successMessage.contains(mb.get("login.reset.success_send")));
        } finally {
            Helper.deleteUser(user.getLogin());
        }
    }

    @Test
    void resetPasswordCheck() {
        User user = UserService.newDefaultUser();
        driver.get(path + "/reset?token=" + user.getToken());

        String email = Helper.genEmail();
        user.setLogin(email);
        user.setPassword(password);
        UserService.save(user);

        try {
            ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver);
            String newPassword = Helper.genPassword();
            resetPasswordPage.enterPassword(newPassword);
            resetPasswordPage.enterConfirmPassword(newPassword);

            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetPasswordCheck_1");
            resetPasswordPage.clickSendButton();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetPasswordCheck_2");

            String successMessage = loginPage.getAlertMessage();
            assertTrue(successMessage.contains(mb.get("login.reset.success_reset")));
        } finally {
            Helper.deleteUser(user.getLogin());
        }
    }

    @Test
    void resetLinkEmptyEmailCheck() {
        driver.get(path);

        loginPage.clickForgotPasswordLink();
        ResetPage resetPage = new ResetPage(driver);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetLinkEmptyEmailCheck_1");
        resetPage.clickSendButton();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetLinkEmptyEmailCheck_2");

        String errorMessage = resetPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.mail.empty")));
    }

    @Test
    void resetLinkUnknownEmailCheck() {
        driver.get(path);

        loginPage.clickForgotPasswordLink();
        ResetPage resetPage = new ResetPage(driver);
        String email = Helper.genEmail();
        resetPage.enterUsername(email);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetLinkUnknownEmailCheck_1");
        resetPage.clickSendButton();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetLinkUnknownEmailCheck_2");

        String errorMessage = resetPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.user.not_found")));
    }

    @Test
    void resetPasswordEmptyPasswordCheck() {
        User user = UserService.newDefaultUser();
        driver.get(path + "/reset?token=" + user.getToken());

        String email = Keys.get("EMAIL.FROM");
        user.setLogin(email);
        user.setPassword(password);
        UserService.save(user);

        try {
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetPasswordEmptyPasswordCheck_1");
            ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver);
            resetPasswordPage.clickSendButton();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetPasswordEmptyPasswordCheck_2");

            String errorMessage = resetPasswordPage.getAlertMessage();
            assertTrue(errorMessage.contains(mb.get("validation.password.empty")));
        } finally {
            Helper.deleteUser(user.getLogin());
        }
    }

    @Test
    void resetPasswordUnknownTokenCheck() {
        String token = StringUtils.randomAlphaString(User.API_KEY_SIZE);
        driver.get(path + "/reset?token=" + token);

        ResetPasswordPage resetPasswordPage = new ResetPasswordPage(driver);
        resetPasswordPage.enterPassword(password);
        resetPasswordPage.enterConfirmPassword(password);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetPasswordUnknownTokenCheck_1");
        resetPasswordPage.clickSendButton();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "resetPasswordUnknownTokenCheck_2");

        String errorMessage = loginPage.getAlertMessage();
        assertTrue(errorMessage.contains("Unknown or invalid token"));
    }

    @Test
    void langCheck() throws Exception {
        driver.get(path);

        MessageBundle messageBundle;
        for (int i = 0; i < 2; i++) {
            if (loginPage
                    .getLangButtonLink()
                    .contains(Lang.EN.toString().toLowerCase(Locale.ROOT))) {
                loginPage.clickLangButton();

                Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "langCheck_" + Lang.EN);
                messageBundle = new MessageBundle(Lang.EN.toString());
            } else if (loginPage
                    .getLangButtonLink()
                    .contains(Lang.UK.toString().toLowerCase(Locale.ROOT))) {
                loginPage.clickLangButton();
                Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "langCheck_" + Lang.UK);
                messageBundle = new MessageBundle(Lang.UK.toString());
            } else {
                throw new Exception("Invalid link in language button");
            }

            assertTrue(messageBundle.get("controls.button.sign_in").equalsIgnoreCase(loginPage.getLogInButtonName()));
            assertEquals(messageBundle.get("controls.button.sign_up"), loginPage.getRegistrationButtonText());
            assertEquals(messageBundle.get("login.form.forgot"), loginPage.getForgotPasswordLinkText());
        }
        driver.get(path + "/lang/" + Keys.get("DEFAULT_LANG").toLowerCase(Locale.ROOT));
    }

    @Test
    void langNegativeCheck() {
        String lang = StringUtils.randomAlphaString(2);
        driver.get(path + "/lang/" + lang);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "langNegativeCheck_" + lang);

        assertTrue(mb.get("controls.button.sign_in").equalsIgnoreCase(loginPage.getLogInButtonName()));
        assertEquals(mb.get("controls.button.sign_up"), loginPage.getRegistrationButtonText());
        assertEquals(mb.get("login.form.forgot"), loginPage.getForgotPasswordLinkText());
    }

    @AfterAll
    static void stop() {
        driver.quit();
        server.stop();
    }
}