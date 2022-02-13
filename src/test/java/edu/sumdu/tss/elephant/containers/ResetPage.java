package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ResetPage {

    public WebDriver driver;

    public ResetPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "//*[@id=\"web-email\"]")
    private WebElement loginField;
    @FindBy(xpath = "/html/body/main/div/div/div/form/div[2]/button")
    private WebElement sendButton;
    @FindBy(xpath = "/html/body/main/div/div/div/div")
    private WebElement alert;

    public void enterUsername(String username) {
        loginField.sendKeys(username);
    }

    public void clickSendButton() {
        sendButton.click();
    }

    public String getAlertMessage() {
        return alert.getText();
    }
}