package uk.sponte.automation.web.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.web.PageElement;
import uk.sponte.automation.web.PageSection;

/**
 * Created by n450777 on 08/04/15.
 */
public class SectionListItem extends PageSection {
    @FindBy(className = "one")
    public PageElement one;

    @FindBy(className = "two")
    public PageElement two;
}
