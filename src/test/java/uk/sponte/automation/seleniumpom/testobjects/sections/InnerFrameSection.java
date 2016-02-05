package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.annotations.Frame;

/**
 * Created by evops on 05/02/2016.
 */
public class InnerFrameSection {
    @Frame
    @FindBy(tagName = "iframe")
    public InnerInnerFrameSection innerInnerFrameSection;
}
