package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageSection;

/**
 * Test section
 * Created by n450777 on 17/03/2017.
 */
public class StaleElementsSection extends PageSection {
    @FindBy(tagName = "p")
    private PageElement paragraph;

    @FindBy(tagName = "button")
    private PageElement button;

    public String getParagraphsText() {
        return paragraph.getText();
    }

    public void triggerStaleElement() {
        button.click();
    }
}
