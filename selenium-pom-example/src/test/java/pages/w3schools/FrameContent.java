package pages.w3schools;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageSection;

/**
 * Created by n450777 on 28/04/2016.
 */
public class FrameContent extends PageSection {
    @FindBy(css = "#main h2")
    public PageElement title;
}
