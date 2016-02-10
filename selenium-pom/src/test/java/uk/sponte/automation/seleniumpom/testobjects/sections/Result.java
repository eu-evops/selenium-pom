package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;

/**
 * Created by evops on 05/02/2016.
 */
public class Result {
    @FindBy(tagName = "h2")
    public PageElement title;
}
