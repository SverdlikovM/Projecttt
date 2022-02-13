package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationPage {

    public WebDriver driver;

    public RegistrationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "//*[@id=\"email\"]")
    private WebElement loginField;
    @FindBy(xpath = "//*[@id=\"password\"]")
    private WebElement passwordField;
    @FindBy(xpath = "//*[@id=\"conformation\"]")
    private WebElement confirmPasswordField;
    @FindBy(xpath = "/html/body/main/div/div/div[2]/form/button")
    private WebElement signUp;
    @FindBy(xpath = "/html/body/main/div/div/div[2]/div")
    private WebElement alert;

    public void enterUsername(String username) {
        loginField.sendKeys(username);
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void enterPasswordConfirmation(String password) {
        confirmPasswordField.sendKeys(password);
    }

    public void signUpButtonClick() {
        signUp.click();
    }

    public String getAlertMessage() {
        return alert.getText();
    }
}