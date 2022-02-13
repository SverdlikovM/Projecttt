package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProfilePage {

    public WebDriver driver;

    public ProfilePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //Buttons
    @FindBy(xpath = "/html/body/div/div/main/div[3]/form/div[2]/button")
    private WebElement regenerateButton_1;
    @FindBy(xpath = "/html/body/div/div/main/div[4]/form/div[2]/button")
    private WebElement regenerateButton_2;
    @FindBy(xpath = "/html/body/div/div/main/div[6]/form/div[2]/button")
    private WebElement changePasswordButton_1;
    @FindBy(xpath = "/html/body/div/div/main/div[7]/form/div[2]/button")
    private WebElement changePasswordButton_2;
    @FindBy(xpath = "/html/body/div/div/main/div[4]/form/div[2]/button")
    private WebElement changeDBPasswordButton;
    @FindBy(xpath = "/html/body/div/div/main/div[6]/div/div[1]/form/div/div[2]/button")
    private WebElement basicUpgradeButton;
    @FindBy(xpath = "/html/body/div/div/main/div[5]/div/div[2]/form/div/div[2]/button")
    private WebElement proUpgradeButton;
    @FindBy(xpath = "/html/body/header/div/a")
    private WebElement logoutButton;

    //Links
    @FindBy(xpath = "/html/body/div/div/main/div[2]/a[1]")
    private WebElement languageEN_1;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/a[1]")
    private WebElement languageEN_2;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/a[2]")
    private WebElement languageUK;
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[1]/a")
    private WebElement goHome;

    //Fields
    @FindBy(xpath = "//*[@id=\"db-username\"]")
    private WebElement dbUsernameField;
    @FindBy(xpath = "//*[@id=\"db-password\"]")
    private WebElement dbPasswordField;
    @FindBy(xpath = "//*[@id=\"web-password\"]")
    private WebElement passwordField;
    @FindBy(xpath = "//*[@id=\"web-password-c\"]")
    private WebElement confirmPasswordField;
    @FindBy(xpath = "//*[@id=\"public-api\"]")
    private WebElement publicApiKeyField;
    @FindBy(xpath = "//*[@id=\"private-api\"]")
    private WebElement privateApiKeyField;


    //Alert
    @FindBy(xpath = "/html/body/div/div/main/div[1]")
    private WebElement alert;

    public void regenerateClick_1() {
        regenerateButton_1.click();
    }

    public void regenerateClick_2() {
        regenerateButton_2.click();
    }

    public void scrollToRegenerate_1() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                languageEN_1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scrollToRegenerate_2() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                languageEN_2);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void changePasswordClick() {
        changePasswordButton_1.click();
    }

    public void changePassword() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                changePasswordButton_2);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        changePasswordButton_2.click();
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        confirmPasswordField.sendKeys(password);
    }

    public void changeDBPasswordClick() {
        scrollToDB();
        changeDBPasswordButton.click();
    }

    public String getDBPassword() {
        return dbPasswordField.getAttribute("value");
    }

    public void enterDBPassword(String password) {
        dbPasswordField.clear();
        dbPasswordField.sendKeys(password);
    }

    public String getDBUsername() {
        return dbUsernameField.getAttribute("value");
    }

    public void scrollToDB() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                regenerateButton_1);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void basicUpgradeClick() {
        scrollToUpgrade();
        basicUpgradeButton.click();
    }

    public void proUpgradeClick() {
        scrollToWebUser();
        proUpgradeButton.click();
    }

    public void scrollToWebUser() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                changePasswordButton_1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scrollToUpgrade() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                basicUpgradeButton);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getLogoutText() {
        return logoutButton.getText();
    }

    public void logout() {
        logoutButton.click();
    }

    public void languageEN_1_click() {
        languageEN_1.click();
    }

    public void languageEN_2_click() {
        languageEN_2.click();
    }

    public void languageUKClick() {
        languageUK.click();
    }

    public String getPublicApiKey() {
        return publicApiKeyField.getAttribute("value");
    }

    public String getPrivateApiKey() {
        return privateApiKeyField.getAttribute("value");
    }

    public String getAlertMessage() {
        return alert.getText();
    }

    public void goHome() {
        goHome.click();
    }
}