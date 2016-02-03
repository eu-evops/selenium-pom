package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;

import java.util.List;

/**
 * Created by evops on 02/02/2016.
 */
public class FrameSubSectionWithList {
    @FindBy(tagName = "li")
    public List<PageElement> items;
}
