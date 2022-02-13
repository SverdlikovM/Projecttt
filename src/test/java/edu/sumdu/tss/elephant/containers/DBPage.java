package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DBPage {

    public WebDriver driver;

    public DBPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //DB
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[1]/div/a")
    private WebElement DBExplorer;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/h1/a[2]")
    private WebElement DBName;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[4]/div/a")
    private WebElement sqlTerminal;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/div[2]")
    private WebElement backup;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[3]/div/a")
    private WebElement scripts;

    //User
    @FindBy(xpath = "/html/body/header/div/span/a")
    private WebElement currentUsername;

    //Alert
    @FindBy(xpath = "/html/body/div/div/main/div[1]")
    private WebElement alert;

    public void DBExplorer() {
        DBExplorer.click();
    }

    public String getDBName() {
        return DBName.getText();
    }

    public void sqlTerminal() {
        sqlTerminal.click();
    }

    public void backup() {
        backup.click();
    }

    public void scripts() {
        scripts.click();
    }

    public void toUserProfile() {
        currentUsername.click();
    }

    public String getAlertMessage() {
        return alert.getText();
    }
}