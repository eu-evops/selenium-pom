package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import uk.sponte.automation.seleniumpom.testobjects.pages.Homepage;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;

import static org.junit.Assert.*;

/**
 * Created by swozniak on 03/04/15.
 */
public class PageTest extends BasePageTest {
    @Test
    public void pageFactoryInitialisationTest() {
        pageFactory.get(Homepage.class);
    }


    @Test
    public void canGetHeadline() {
        assertEquals("Headline", testPage.headline.getText());
    }

    @Test
    public void canSetTextField() {
        String testString = "Testing";
        testPage.someInput.set(testString);
        assertEquals(testString, testPage.someInput.getValue());
    }

    @Test
    public void canDoubleClickElement() {
        testPage.doubleClick.doubleClick();
        assertEquals("Double clicked", testPage.doubleClick.getText());
    }

    @Test
    public void canDragAndDrop() {
        testPage.drag.dropOnto(testPage.drop);
        assertEquals("Dropped!", testPage.drop.getText());
    }

    @Test
    public void canInstantiatePageWhenBrowserDoesNotHaveRequestedElements() {
        driver.navigate().to("about:blank");
        testPage = pageFactory.get(TestPage.class);
    }

    @Test
    public void waitsForElement() {
        testPage.createElementAfterDelay.click();
        testPage.newElement.waitFor();

        assertEquals("Hello World", testPage.newElement.getText());
    }

    @Test(expected = TimeoutException.class)
    public void timesOutWaitingForAnElement() {
        testPage.createElementAfterDelay.click();
        testPage.newElement.waitFor(SHORT_TIMEOUT);
    }

    @Test
    public void waitsForAnElementToBeGone() {
        testPage.removeElementAfterDelay.click();
        testPage.headline.waitUntilGone();
    }

    @Test(expected = TimeoutException.class)
    public void timesOutWaitingForAnElementToBeGone() {
        testPage.removeElementAfterDelay.click();

        testPage.headline.waitUntilGone(SHORT_TIMEOUT);
    }

    @Test
    public void waitsforElementToBeHidden() {
        testPage.hideElementAfterDelay.click();
        testPage.headline.waitUntilHidden();
    }

    @Test(expected = TimeoutException.class)
    public void timesOutWhileWaitingforElementToBeHidden() {
        testPage.hideElementAfterDelay.click();
        testPage.headline.waitUntilHidden(SHORT_TIMEOUT);
    }

    @Test
    public void waitForElementToBecomeVisible() {
        testPage.showElementAfterDelay.click();
        testPage.subtitle.waitUntilVisible();
    }

    @Test(expected = TimeoutException.class)
    public void timesOutWhileWaitingForElementToBecomeVisible() {
        testPage.showElementAfterDelay.click();
        testPage.subtitle.waitUntilVisible(SHORT_TIMEOUT);
    }


    @Test
    public void mapsPageSectionCorrectly() {
        assertNotNull("Page section should not be null", testPage.parent);
        assertNotNull("Page section should not be null", testPage.parent.child);
    }

    @Test
    public void canGetPageSection() {
        assertEquals("There should only be 1 child element", 1, testPage.parent.child.children.size());
    }
    @Test
    public void returnsAListOfElements() {
        assertEquals("There should be 9 child elements", 9, testPage.listPageElements.size());
    }

    @Test
    public void canGetSectionAndItsContents() {
        assertEquals("Section heading", testPage.section.headline.getText());
        assertEquals("Section subtitle", testPage.section.subtitle.getText());
    }

    @Test
    public void canGetListOfChildSections() {
        assertEquals(2, testPage.sectionList.size());
        assertEquals("one-one", testPage.sectionList.get(0).one.getText());
        assertEquals("one-two", testPage.sectionList.get(0).two.getText());
        assertEquals("two-one", testPage.sectionList.get(1).one.getText());
        assertEquals("two-two", testPage.sectionList.get(1).two.getText());
    }

    @Test
    public void canCheckWhetherSectionExists() {
        assertTrue(testPage.section.isPresent());
    }


    @Test
    public void setsRootElementForAChildSectionIfParentDoesNotExtendPageSection() {
        assertNotNull(testPage.plainClassSection);
        assertNotNull(testPage.plainClassSection.childInheritingFromPageSection);

        assertEquals("one", testPage.plainClassSection.childInheritingFromPageSection.children.get(0).getText());
    }

    @Test
    public void canUseListOfLists() {
        assertEquals(2, testPage.listItems.size());
        assertNotNull(testPage.listItems.get(0).subItems);

        assertEquals(3, testPage.listItems.get(0).subItems.size());

        assertEquals("List 1: Item 1", testPage.listItems.get(0).subItems.get(0).getText());
        assertEquals("List 2: Item 3", testPage.listItems.get(1).subItems.get(2).getText());
    }

    @Test
    public void canUsePrivateField() {
        assertEquals("Headline", testPage.getPrivateHeadlineContents());
    }

    @Test
    public void verifyElementToBeClickable(){
        assertEquals(true, Boolean.valueOf(testPage.lateLoadingButton.getAttribute("disabled")));
        testPage.lateLoadingButton.waitUntilClickable();
        assertEquals(null, testPage.lateLoadingButton.getAttribute("disabled"));
    }
}
