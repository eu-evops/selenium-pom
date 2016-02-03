package uk.sponte.automation.seleniumpom.testobjects.pages;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.testobjects.sections.FrameSubSection;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.annotations.Frame;
import uk.sponte.automation.seleniumpom.annotations.Section;
import uk.sponte.automation.seleniumpom.testobjects.sections.FrameSubSectionWithList;

/**
 * Created by evops on 02/02/2016.
 */
@Frame()
@FindBy(tagName = "iframe")
public class PageInsideFrame {
    @FindBy(tagName = "h1")
    public PageElement headline;

    @Section
    public FrameSubSection frameSubSection;

    @Section
    @FindBy(id = "subsection")
    public FrameSubSectionWithList frameSubSectionWithList;

}
