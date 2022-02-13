package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BackupPage {

    public WebDriver driver;

    public BackupPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //Buttons
    @FindBy(xpath = "/html/body/div/div/main/div[3]/form/button")
    private WebElement createButton_1;
    @FindBy(xpath = "/html/body/div/div/main/div[4]/form/button")
    private WebElement createButton_2;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[2]/div/div[2]/div/form[3]/input")
    private WebElement deleteButton_1;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/div[2]/div/div[2]/div/form[3]/input")
    private WebElement deleteButton_2;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[2]/div/div[2]/div/form[1]/input")
    private WebElement updateButton_1;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/div[2]/div/div[2]/div/form[1]/input")
    private WebElement updateButton_2;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[2]/div/div[2]/div/form[2]/input")
    private WebElement resetButton_1;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/div[2]/div/div[2]/div/form[2]/input")
    private WebElement resetButton_2;
    @FindBy(xpath = "/html/body/header/div/a")
    private WebElement logoutButton;

    //Links
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[3]/a")
    private WebElement scripts;
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[4]/a")
    private WebElement sqlTerminal;
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[1]/a")
    private WebElement DBExplorer;
    @FindBy(xpath = "/html/body/header/div/span/a")
    private WebElement currentUsername;

    @FindBy(xpath = "//*[@id=\"floatingInput\"]")
    private WebElement nameField;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[2]/div/div[1]/h6")
    private WebElement pointName;
    @FindBy(xpath = "/html/body/div/div/main/div[1]")
    private WebElement alert;

    public void create_1() {
        createButton_1.click();
    }

    public void create_2() {
        createButton_2.click();
    }

    public void delete_1() {
        deleteButton_1.click();
    }

    public void delete_2() {
        deleteButton_2.click();
    }

    public void update_1() {
        updateButton_1.click();
    }

    public void update_2() {
        updateButton_2.click();
    }

    public void reset_1() {
        resetButton_1.click();
    }

    public void reset_2() {
        resetButton_2.click();
    }

    public void logout() {
        logoutButton.click();
    }

    public void scripts() {
        scripts.click();
    }

    public void sqlTerminal() {
        sqlTerminal.click();
    }

    public void DBExplorer() {
        DBExplorer.click();
    }

    public void toUserProfile() {
        currentUsername.click();
    }

    public void enterName(String value) {
        nameField.sendKeys(value);
    }


    public String getPointName() {
        return pointName.getText();
    }

    public String getAlertMessage() {
        return alert.getText();
    }
}