package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageSection;

/**
 * Created by n450777 on 08/04/15.
 */
public class SectionListItem extends PageSection {
    @FindBy(className = "one")
    public PageElement one;

    @FindBy(className = "two")
    public PageElement two;
}
