package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageSection;

import java.util.List;

/**
 * Created by n450777 on 07/04/15.
 */
public class ChildSection extends PageSection {
    @FindBy(className = "item")
    public List<PageElement> children;
}
