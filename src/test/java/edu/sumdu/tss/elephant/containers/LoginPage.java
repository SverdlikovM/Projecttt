package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {

    public WebDriver driver;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //buttons and links
    @FindBy(xpath = "/html/body/main/div[1]/div/div[2]/form/button")
    private WebElement logInButton;
    @FindBy(xpath = "/html/body/header/div/a[3]")
    private WebElement registerButton;
    @FindBy(xpath = "/html/body/main/div[1]/div/div[2]/form/p/a")
    private WebElement forgotPasswordLink;
    @FindBy(xpath = "/html/body/header/div/a[1]")
    private WebElement langButton;

    //fields
    @FindBy(xpath = "//*[@id=\"floatingInput\"]")
    private WebElement loginField;
    @FindBy(xpath = "//*[@id=\"floatingPassword\"]")
    private WebElement passwordField;

    //alert
    @FindBy(xpath = "/html/body/main/div[1]/div/div[2]/div")
    private WebElement alert;

    public void enterUsername(String username) {
        loginField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void clickLogInButton() {
        logInButton.click();
    }

    public void clickForgotPasswordLink() {
        forgotPasswordLink.click();
    }

    public void clickLangButton() {
        langButton.click();
    }

    public void registrationButtonClick() {
        registerButton.click();
    }

    public String getAlertMessage() {
        return alert.getText();
    }

    public String getLangButtonLink() {
        return langButton.getAttribute("href");
    }

    public String getLogInButtonName() {
        return logInButton.getText();
    }

    public String getForgotPasswordLinkText() {
        return forgotPasswordLink.getText();
    }

    public String getRegistrationButtonText() {
        return registerButton.getText();
    }
}