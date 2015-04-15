package uk.sponte.automation.web.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.web.PageElement;
import uk.sponte.automation.web.PageSection;

/**
 * Created by n450777 on 13/04/15.
 */
public class ListSubItem extends PageSection {
    @FindBy(tagName = "h5") public PageElement headline;
}
