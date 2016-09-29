package uk.sponte.automation.seleniumpom.testobjects.pages;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;

/**
 * Test page object
 */
public class Homepage {
    PageElement loginButton;

    @FindBy(id = "something")
    public PageElement something;
}
