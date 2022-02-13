package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ErrorPage {

    public WebDriver driver;

    public ErrorPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "/html/body/main/div/div/div[2]/h1")
    private WebElement errorCode;
    @FindBy(xpath = "/html/body/main/div/div/div[2]/p")
    private WebElement errorMessage;
    @FindBy(xpath = "/html/body/main/div/div/div[2]/a")
    private WebElement goBack;

    public int getErrorCode() {
        return Integer.parseInt(errorCode.getText());
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public void goBack() {
        goBack.click();
    }
}