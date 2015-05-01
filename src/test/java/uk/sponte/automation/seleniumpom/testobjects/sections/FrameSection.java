package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;

/**
 * Created by n450777 on 30/04/15.
 */
public class FrameSection {
    @FindBy(tagName = "h1")
    public PageElement headline;
}
