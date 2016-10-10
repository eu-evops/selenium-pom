package uk.sponte.automation.seleniumpom.testobjects.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.testobjects.sections.ListItem;
import uk.sponte.automation.seleniumpom.testobjects.sections.TestSection;

import java.util.List;

/**
 * Created by n450777 on 09/03/2016.
 */
public class CachingTestPage {

    @FindBy(id = "section")
    public WebElement webElement;

    @FindBy(id = "section")
    public PageElement pageElement;

    @FindBy(id = "section")
    public TestSection pageSection;

    @FindBy(className = "listItem")
    public List<ListItem> listItems;
}
