package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageSection;

/**
 * Created by n450777 on 13/04/15.
 */
public class ListSubItem extends PageSection {
    @FindBy(tagName = "h5") public PageElement headline;
}
