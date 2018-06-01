package uk.sponte.automation.seleniumpom.testobjects.pages;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.testobjects.sections.ListSubItem;
import uk.sponte.automation.seleniumpom.testobjects.sections.ParentSection;
import uk.sponte.automation.seleniumpom.testobjects.sections.SectionListItem;
import uk.sponte.automation.seleniumpom.testobjects.sections.StaleElementsSection;
import uk.sponte.automation.seleniumpom.testobjects.sections.TestSection;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.annotations.Frame;
import uk.sponte.automation.seleniumpom.annotations.Section;
import uk.sponte.automation.seleniumpom.testobjects.sections.FrameSection;
import uk.sponte.automation.seleniumpom.testobjects.sections.ListItem;
import uk.sponte.automation.seleniumpom.testobjects.sections.PlainSection;

import java.util.List;

/**
 * Test page for all UI tests
 * Created by swozniak-ba on 02/04/15.
 */
public class TestPage implements ITestPage {
    @FindBy(tagName = "h1")
    public PageElement headline;

    @FindBy(tagName = "h1")
    private PageElement privateHeadline;

    @FindBy(id = "staleElementsTests")
    public StaleElementsSection staleElementsSection;

    @FindBy(tagName = "h2")
    public PageElement subtitle;

    public PageElement someInput;

    @FindBy(id = "dblClick")
    public PageElement doubleClick;

    @FindBy(id="lateLoadingButton")
    public PageElement lateLoadingButton;


    public PageElement drag;
    public PageElement drop;

    public PageElement createElementAfterDelay;
    public PageElement removeElementAfterDelay;
    public PageElement hideElementAfterDelay;
    public PageElement showElementAfterDelay;

    public PageElement newElement;

    @FindBy(className = "item")
    public List<PageElement> listPageElements;

    /**
     * Describes a section on the page, group of elements and other
     * sections.
     */
    @Section public ParentSection parent;

    public TestSection section;

    @FindBy(id = "plainClassSection")
    public PlainSection basedOnFindByAnnotation;

    @FindBy(className = "sectionListItem")
    @Section public List<SectionListItem> sectionList;
    @Section public PlainSection plainClassSection;


    @FindBy(className = "sectionListItem")
    public List<SectionListItem> sectionListWithoutAnnotation;

    @FindBy(className = "listItem")
    @Section public List<ListItem> listItems;

    public PageElement hidden;

    @FindBy(css = ".longList li")
    public List<PageElement> longListElements;

    @FindBy(css = ".longList li")
    public List<ListSubItem> longListSections;

    @Frame()
    @FindBy(tagName = "iframe")
    public FrameSection iframe;


    @Override
    public String getPrivateHeadlineContents() {
        return this.privateHeadline.getText();
    }
}
