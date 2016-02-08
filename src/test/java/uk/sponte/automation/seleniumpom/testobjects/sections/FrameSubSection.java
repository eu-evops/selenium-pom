package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.annotations.Section;

import java.util.List;

/**
 * Created by n450777 on 01/05/15.
 */
public class FrameSubSection {
    @FindBy(css = "h2.inner")
    public PageElement title;


    @FindBy(id = "subsection")
    public FrameSubSectionWithList frameSubSectionWithList;

    @Section
    @FindBy(css = ".resultSection")
    public List<ResultSection> resultSections;
}
