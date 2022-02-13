package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SqlTerminalPage {

    public WebDriver driver;

    public SqlTerminalPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //Buttons
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div[1]/button")
    private WebElement runButton;
    @FindBy(xpath = "/html/body/header/div/a")
    private WebElement logoutButton;

    //Fields
    @FindBy(xpath = "//*[@id=\"editor\"]/textarea")
    private WebElement textField;
    @FindBy(xpath = "//*[@id=\"DB150_content\"]/div[1]")
    private WebElement resultField_1;
    @FindBy(xpath = "//*[@id=\"DB150_content\"]/div[2]")
    private WebElement resultField_2;
    @FindBy(xpath = "//*[@id=\"DB150_content\"]/div[3]")
    private WebElement resultField_3;

    //Links
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[1]/a")
    private WebElement DBExplorer;

    public void runButtonClick() {
        runButton.click();
    }

    public void logout() {
        logoutButton.click();
    }

    public void DBExplorer() {
        DBExplorer.click();
    }

    public void enterSqlCommand(String command) {
        textField.sendKeys(command);
    }

    public String getResultMessage_1() {
        return resultField_1.getText();
    }

    public String getResultMessage_2() {
        return resultField_2.getText();
    }

    public String getResultMessage_3() {
        return resultField_3.getText();
    }

    public void scroll() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                runButton);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}