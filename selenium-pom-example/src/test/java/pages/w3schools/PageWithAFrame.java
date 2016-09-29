package pages.w3schools;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.annotations.Frame;

/**
 * Created by n450777 on 28/04/2016.
 */
public class PageWithAFrame {
    @Frame
    @FindBy(css = "iframe[src=\"default.asp\"]")
    public FrameContent frameContent;
}
