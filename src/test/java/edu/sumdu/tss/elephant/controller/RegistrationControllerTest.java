package edu.sumdu.tss.elephant.controller;

import java.io.File;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

import edu.sumdu.tss.elephant.Helper;
import edu.sumdu.tss.elephant.Server;
import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.UserRole;
import edu.sumdu.tss.elephant.helper.utils.MessageBundle;
import edu.sumdu.tss.elephant.helper.utils.StringUtils;
import edu.sumdu.tss.elephant.model.User;
import edu.sumdu.tss.elephant.model.DbUserService;
import edu.sumdu.tss.elephant.model.UserService;
import edu.sumdu.tss.elephant.containers.RegistrationPage;
import edu.sumdu.tss.elephant.containers.HomePage;
import edu.sumdu.tss.elephant.containers.ErrorPage;

public class RegistrationControllerTest {

    private static Server server;
    private static WebDriver driver;
    private static MessageBundle mb;
    private static String path;
    private static RegistrationPage registrationPage;
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
        registrationPage = new RegistrationPage(driver);
        homePage = new HomePage(driver);

        path = Keys.get("APP.URL") + "/registration";
        mb = new MessageBundle(Keys.get("DEFAULT_LANG"));
    }

    @Test
    void noEmailCheck() {
        driver.get(path);

        registrationPage.enterPassword(password);
        registrationPage.enterPasswordConfirmation(password);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "noEmailCheck_1");
        registrationPage.signUpButtonClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "noEmailCheck_2");

        String errorMessage = registrationPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.mail.empty")));
    }

    @Test
    void noPasswordCheck() {
        driver.get(path);

        String email = Helper.genEmail();
        registrationPage.enterUsername(email);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "noPasswordCheck_1");
        registrationPage.signUpButtonClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "noPasswordCheck_2");

        String errorMessage = registrationPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.password.empty")));
    }

    @Test
    void wrongFormatEmailCheck() {
        driver.get(path);

        registrationPage.enterUsername(password);
        registrationPage.enterPassword(password);
        registrationPage.enterPasswordConfirmation(password);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "wrongFormatEmailCheck_1");
        registrationPage.signUpButtonClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "wrongFormatEmailCheck_2");

        String errorMessage = registrationPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.mail.invalid")));
    }

    @Test
    void wrongFormatPasswordCheck() {
        driver.get(path);

        String email = Helper.genEmail();
        registrationPage.enterUsername(email);
        String password = StringUtils.randomAlphaString(8);
        registrationPage.enterPassword(password);
        registrationPage.enterPasswordConfirmation(password);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "wrongFormatPasswordCheck_1");
        registrationPage.signUpButtonClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "wrongFormatPasswordCheck_2");

        String errorMessage = registrationPage.getAlertMessage();
        assertTrue(errorMessage.contains(mb.get("validation.password.invalid")));
    }

    @Test
    void existingEmailCheck() {
        User user = UserService.newDefaultUser();
        String email = Helper.genEmail();
        user.setLogin(email);
        user.setPassword(password);
        UserService.save(user);

        try {
            driver.get(path);

            registrationPage.enterUsername(email);
            registrationPage.enterPassword(password);
            registrationPage.enterPasswordConfirmation(password);

            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "existingEmailCheck_1");
            registrationPage.signUpButtonClick();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "existingEmailCheck_2");

            assertTrue(registrationPage.getAlertMessage().contains("Login (email) already taken"));
        } finally {
            deleteUser(user);
        }
    }

    @Test
    void confirmationLinkCheck() {
        User user = UserService.newDefaultUser();
        String email = Helper.genEmail();
        user.setLogin(email);
        user.setPassword(password);
        UserService.save(user);
        UserService.initUserStorage(user.getUsername());
        DbUserService.initUser(user.getUsername(), user.getDbPassword());

        try {
            driver.get(path + "/confirm/" + user.getToken());

            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "confirmationLinkCheck");
            user = UserService.byLogin(email);
            String successMessage = homePage.getAlertMessage();

            homePage.logout();
            assertEquals(UserRole.BASIC_USER, user.role());
            assertEquals("Email approved", successMessage);
        } finally {
            deleteUser(user);
        }
    }

    @Test
    void confirmationLinkNegativeCheck() {
        final int errorCode = 404;

        String token = StringUtils.randomAlphaString(User.API_KEY_SIZE);
        ErrorPage errorPage = new ErrorPage(driver);
        driver.get(path + "/confirm/" + token);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "confirmationLinkNegativeCheck");
        assertEquals(errorCode, errorPage.getErrorCode());
        assertTrue(errorPage.getErrorMessage().contains("not found"));
    }

    @Test
    void signUpResendCheck() {
        driver.get(path);

        String email = Keys.get("EMAIL.FROM");
        registrationPage.enterUsername(email);
        registrationPage.enterPassword(password);
        registrationPage.enterPasswordConfirmation(password);

        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "signUpResendCheck_1");
        registrationPage.signUpButtonClick();
        Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "signUpResendCheck_2");

        User user = UserService.byLogin(email);
        try {
            assertEquals(email, homePage.getCurrentUsername());
            assertEquals(user.crypt(password), user.getPassword());
            assertEquals(UserRole.UNCHEKED, user.role());

            homePage.resendList();
            Helper.takeScreenshot(driver, this.getClass().getSimpleName(), "signUpResendCheck_3");

            assertEquals("Resend conformation email", homePage.getAlertMessage());
        } finally {
            homePage.logout();
            deleteUser(user);
        }
    }

    void deleteUser(User user) {
        Helper.dropTablespace(user.getUsername());
        //db user name
        Helper.dropUser(user.getUsername());
        Helper.deleteUser(user.getLogin());
    }

    @AfterAll
    static void stop() {
        driver.quit();
        server.stop();
    }
}