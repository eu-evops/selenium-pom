package uk.sponte.automation.web.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.web.PageElement;
import uk.sponte.automation.web.PageSection;

import java.util.List;

/**
 * Created by n450777 on 07/04/15.
 */
public class ChildSection extends PageSection {
    @FindBy(className = "item")
    public List<PageElement> children;
}
