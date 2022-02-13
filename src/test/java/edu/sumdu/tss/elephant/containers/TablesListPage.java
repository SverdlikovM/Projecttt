package edu.sumdu.tss.elephant.containers;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TablesListPage {

    public WebDriver driver;

    public TablesListPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //Links
    @FindBy(xpath = "/html/body/div/div/main/table/tbody/tr[2]/td/a")
    private WebElement tableButton;
    @FindBy(xpath = "/html/body/header/div/a")
    private WebElement logoutButton;

    public void tableButtonClick() {
        tableButton.click();
    }

    public String getTableName() {
        return tableButton.getText();
    }

    public void logout() {
        logoutButton.click();
    }
}