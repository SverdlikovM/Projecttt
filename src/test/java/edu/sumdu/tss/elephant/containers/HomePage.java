package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage {

    public WebDriver driver;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //Auth
    @FindBy(xpath = "/html/body/header/div/span/a")
    private WebElement currentAuthUsername;
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[2]/a")
    private WebElement profileLink;
    @FindBy(xpath = "/html/body/header/div/a")
    private WebElement logoutButton;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/a")
    private WebElement resendListButton;
    @FindBy(xpath = "/html/body/div/div/main/div[1]")
    private WebElement alert;

    //DB
    @FindBy(xpath = "/html/body/div/div/main/div[3]/form/button")
    private WebElement createDBButton_1;
    @FindBy(xpath = "/html/body/div/div/main/div[4]/form/button")
    private WebElement createDBButton_2;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/button")
    private WebElement createDBButtonLimitReached;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[2]/div/div[2]/div/form/button")
    private WebElement deleteDBButton;
    @FindBy(xpath = "/html/body/div/div/main/div[3]/div/div[2]/div/div[1]/a")
    private WebElement DBName;
    @FindBy(xpath = "/html/body/div/div/main/div[3]")
    private WebElement fullDBPlan;
    @FindBy(xpath = "/html/body/div/div/main/div[2]/div/div[2]/div/div/div[1]/h2/span[1]")
    private WebElement maxDBPlan;
    @FindBy(xpath = "/html/body/div/div/main/div[4]/div/div[2]/p")
    private WebElement noDB;

    //Auth

    public String getCurrentUsername() {
        return currentAuthUsername.getText();
    }

    public void logout() {
        logoutButton.click();
    }

    public String getAlertMessage() {
        return alert.getText();
    }

    public void resendList() {
        resendListButton.click();
    }

    public void goToUserProfile() {
        profileLink.click();
    }

    //DB

    public void createDB_1() {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);",
                        createDBButton_1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        createDBButton_1.click();
    }

    public void createDB_2() {
        createDBButton_2.submit();
    }

    public boolean createDBIsEnabled() {
        return createDBButtonLimitReached.isEnabled();
    }

    public void deleteDB() {
        deleteDBButton.click();
    }

    public String getDatabaseName() {
        return DBName.getText();
    }

    public void goToDB() {
        DBName.click();
    }

    public String fullDBPlanMessage() {
        return fullDBPlan.getText();
    }

    public String maxDBPlans() {
        return maxDBPlan.getText();
    }

    public WebElement noDB() {
        return this.noDB;
    }

    public void scrollToCreateButton() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
                createDBButtonLimitReached);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}