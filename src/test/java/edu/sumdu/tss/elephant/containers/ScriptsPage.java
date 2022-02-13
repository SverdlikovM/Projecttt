package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;

public class ScriptsPage {

    public WebDriver driver;

    public ScriptsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //Buttons
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/div[2]/div/div[2]/div/form[1]/button")
    private WebElement runScriptButton;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/div[2]/div/div[2]/div/form[2]/button")
    private WebElement deleteScriptButton;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/form/button")
    private WebElement uploadScriptButton;
    @FindBy(xpath = "/html/body/header/div/a")
    private WebElement logoutButton;

    //Links
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[1]/a")
    private WebElement DBExplorer;
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[3]/a")
    private WebElement scriptPage;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/div[2]/div/div[1]/a")
    private WebElement script;

    @FindBy(xpath = "/html/body/div/div/main/div[3]/form/div[2]/input")
    private WebElement loadScript;

    //Alert
    @FindBy(xpath = "/html/body/div/div/main/div[1]")
    private WebElement alert;

    public void runScript() {
        runScriptButton.click();
    }

    public void deleteScript() {
        deleteScriptButton.click();
    }

    public void uploadScript() {
        uploadScriptButton.click();
    }

    public void loadScript(File file) {
        loadScript.sendKeys(file.getAbsolutePath());
    }

    public String getScriptName() {
        return script.getText();
    }

    public void scriptPage() {
        scriptPage.click();
    }

    public void DBExplorer() {
        DBExplorer.click();
    }

    public void logout() {
        logoutButton.click();
    }

    public String getAlertMessage() {
        return alert.getText();
    }
}