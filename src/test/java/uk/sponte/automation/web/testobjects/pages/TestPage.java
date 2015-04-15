package uk.sponte.automation.web.testobjects.pages;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.web.PageElement;
import uk.sponte.automation.web.annotations.Section;
import uk.sponte.automation.web.testobjects.sections.*;

import java.util.List;

/**
 * Created by swozniak-ba on 02/04/15.
 */
public class TestPage {
    @FindBy(tagName = "h1")
    public PageElement headline;

    @FindBy(tagName = "h2")
    public PageElement subtitle;

    public PageElement someInput;

    @FindBy(id = "dblClick")
    public PageElement doubleClick;

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


    @Section public TestSection section;

    @FindBy(className = "sectionListItem")
    @Section public List<SectionListItem> sectionList;
    @Section public PlainSection plainClassSection;

    @FindBy(className = "listItem")
    @Section public List<ListItem> listItems;

    public PageElement hidden;
}
