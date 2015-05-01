package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;

/**
 * Created by n450777 on 01/05/15.
 */
public class FrameSubSection {
    @FindBy(tagName = "h2")
    public PageElement title;
}
