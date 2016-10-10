package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.StaleElementReferenceException;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.testobjects.pages.CachingTestPage;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;
import uk.sponte.automation.seleniumpom.testobjects.sections.ListSubItem;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Caching tests - test caching features of selenium-pom framework
 * Created by n450777 on 04/03/2016.
 */
public class CachingTest extends BasePageTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void throwsStaleElementException() {
        thrown.expect(StaleElementReferenceException.class);

        testPage.headline.getText();
        driver.navigate().refresh();
        testPage.headline.getText();

        pageFactory.get(TestPage.class).headline.getText();
    }

    @Test
    public void canRefreshModel() {
        CachingTestPage cachingTestPage = pageFactory
                .get(CachingTestPage.class);

        String webElementText = cachingTestPage.webElement.getText();
        String pageElementText = cachingTestPage.pageElement.getText();
        String sectionText = cachingTestPage.pageSection.getText();
        int oneLevelListSize = cachingTestPage.listItems.size();
        String listItemText = cachingTestPage.listItems.get(1).getText();

        driver.navigate().refresh();
        pageFactory.invalidate(cachingTestPage);

        assertEquals(webElementText, cachingTestPage.webElement.getText());
        assertEquals(pageElementText, cachingTestPage.pageElement.getText());
        assertEquals(sectionText, cachingTestPage.pageSection.getText());
        assertEquals(oneLevelListSize, cachingTestPage.listItems.size());
        assertEquals(listItemText, cachingTestPage.listItems.get(1).getText());

        String headlineText = cachingTestPage.pageSection.headline.getText();
        String elementText = cachingTestPage.pageSection.listItems.get(0).getText();
        List<ListSubItem> cachedListOfItems = cachingTestPage.listItems.get(0).subItems;
        PageElement headline = cachingTestPage.listItems.get(1).subItems.get(1).headline;

        driver.navigate().refresh();

        pageFactory.invalidate(cachingTestPage);

        assertEquals(headlineText, cachingTestPage.pageSection.headline.getText());
        assertNotNull(cachingTestPage.pageSection.getText());
        assertEquals(3, cachedListOfItems.size());
        assertEquals("List 2: Item 2", headline.getText());
        assertEquals(elementText, cachingTestPage.pageSection.listItems.get(0).getText());
    }

    @Test
    public void returnsNewElementsAfterInvalidate() {
        CachingTestPage cachingTestPage = pageFactory
                .get(CachingTestPage.class);

        // Need to access first to trigger element finding
        assertEquals(3, cachingTestPage.pageSection.listItems.size());

        cachingTestPage.pageSection.addNewItemButton.click();
        pageFactory.invalidate(cachingTestPage);

        assertEquals("New Element", cachingTestPage.pageSection.listItems.get(3).getText());
        assertEquals(4, cachingTestPage.pageSection.listItems.size());
    }

    @Test
    public void returnsNewElementsAfterInvalidateWhileAccessingOriginalItemsFirst() {
        CachingTestPage cachingTestPage = pageFactory
                .get(CachingTestPage.class);

        // Need to access first to trigger element finding
        assertEquals(3, cachingTestPage.pageSection.listItems.size());

        cachingTestPage.pageSection.addNewItemButton.click();
        pageFactory.invalidate(cachingTestPage);

        // Can still read elements
        assertEquals("three", cachingTestPage.pageSection.listItems.get(2).getText());
        assertEquals("New Element", cachingTestPage.pageSection.listItems.get(3).getText());
        assertEquals(4, cachingTestPage.pageSection.listItems.size());
    }

    @Test
    public void returnsMultipleNewElementsAfterInvalidateWhileAccessingOriginalItemsFirst() {
        CachingTestPage cachingTestPage = pageFactory
                .get(CachingTestPage.class);

        // Need to access first to trigger element finding
        assertEquals(3, cachingTestPage.pageSection.listItems.size());

        cachingTestPage.pageSection.addTwoNewItemsButton.click();
        pageFactory.invalidate(cachingTestPage);

        assertEquals("New Element", cachingTestPage.pageSection.listItems.get(3).getText());
        assertEquals("New Element 2", cachingTestPage.pageSection.listItems.get(4).getText());
        assertEquals(5, cachingTestPage.pageSection.listItems.size());
    }

    @Test
    public void returnsNewListWithLessElementsThanOriginalAfterRemovingItems() {
        CachingTestPage cachingTestPage = pageFactory
                .get(CachingTestPage.class);

        // Need to access first to trigger element finding
        assertEquals(3, cachingTestPage.pageSection.listItems.size());

        cachingTestPage.pageSection.removeItemButton.click();
        pageFactory.invalidate(cachingTestPage);

        assertEquals("two", cachingTestPage.pageSection.listItems.get(1).getText());
        assertEquals(2, cachingTestPage.pageSection.listItems.size());
    }
}
