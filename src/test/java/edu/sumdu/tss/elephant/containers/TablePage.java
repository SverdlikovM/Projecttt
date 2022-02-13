package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TablePage {

    public WebDriver driver;

    public TablePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //Buttons
    @FindBy(xpath = "/html/body/header/div/a")
    private WebElement logoutButton;
    @FindBy(xpath = "/html/body/div/div/main/h1")
    private WebElement tableButton;

    //Links
    @FindBy(xpath = "/html/body/div/div/main/nav/ul/li[2]/a")
    private WebElement nextPage;
    @FindBy(xpath = "//*[@id=\"sidebarMenu\"]/div/ul/li[2]/a")
    private WebElement backup;

    //Table
    @FindBy(xpath = "/html/body/div/div/main/table/tbody/tr[2]/td[1]")
    private WebElement tableValueLeft;
    @FindBy(xpath = "/html/body/div/div/main/table/tbody/tr[2]/td[2]")
    private WebElement tableValueRight;

    public void logout() {
        logoutButton.click();
    }

    public String getTableName() {
        return tableButton.getText();
    }

    public void goToNextPage() {
        nextPage.click();
    }

    public void backup() {
        backup.click();
    }

    public String getTableValueLeft() {
        return tableValueLeft.getText();
    }

    public String getTableValueRight() {
        return tableValueRight.getText();
    }
}