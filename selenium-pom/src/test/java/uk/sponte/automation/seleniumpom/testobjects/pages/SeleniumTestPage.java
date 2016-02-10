package uk.sponte.automation.seleniumpom.testobjects.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by swozniak-ba on 02/04/15.
 * Selenium test page
 */
public class SeleniumTestPage {
    @FindBy(tagName = "h1")
    public WebElement headline;

    @FindBy(className = "item")
    public List<WebElement> listPageElements;
}
