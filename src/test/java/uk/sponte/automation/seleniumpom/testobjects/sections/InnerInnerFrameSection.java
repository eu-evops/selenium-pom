package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;

import java.util.concurrent.TimeoutException;

/**
 * Created by evops on 05/02/2016.
 */
public class InnerInnerFrameSection {
    @FindBy(tagName = "h2")
    public PageElement title;

    @FindBy(id = "createElementAfterDelay")
    private PageElement createElementAfterDelay;

    public PageElement newElement;

    public PageElement clickAndWaitForNewContent() throws TimeoutException {
        this.createElementAfterDelay.click();
        newElement.waitFor();
        return newElement;
    }
}
