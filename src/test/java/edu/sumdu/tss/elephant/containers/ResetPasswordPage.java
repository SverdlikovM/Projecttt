package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ResetPasswordPage {

    public WebDriver driver;

    public ResetPasswordPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "//*[@id=\"web-password\"]")
    private WebElement passwordField;
    @FindBy(xpath = "//*[@id=\"web-password-c\"]")
    private WebElement confirmPasswordField;
    @FindBy(xpath = "/html/body/main/div/div/div/form/div[3]/button")
    private WebElement sendButton;
    @FindBy(xpath = "/html/body/main/div/div/div/div")
    private WebElement alert;

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        confirmPasswordField.sendKeys(password);
    }

    public void clickSendButton() {
        sendButton.click();
    }

    public String getAlertMessage() {
        return alert.getText();
    }
}