package uk.sponte.automation.web.tests.ui;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.web.PageFactory;
import uk.sponte.automation.web.PageSection;
import uk.sponte.automation.web.dependencies.DefaultDependencyInjectorImpl;
import uk.sponte.automation.web.helpers.TestHelper;
import uk.sponte.automation.web.testobjects.pages.TestPage;

import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.*;

/**
 * Created by swozniak on 03/04/15.
 */
public class PageTest {

    public static final int SHORT_TIMEOUT = 200;
    private static WebDriver driver;
    private static PageFactory pageFactory;
    private static String url;
    private TestPage testPage;

    @BeforeClass
    public static void setup() {
        TestHelper testHelper = new TestHelper();
        url = testHelper.getTestPageAsBase64();

        DefaultDependencyInjectorImpl defaultDependencyInjector = new DefaultDependencyInjectorImpl();
        driver = defaultDependencyInjector.get(WebDriver.class);
        pageFactory = new PageFactory(defaultDependencyInjector);
    }

    @Before
    public void navigateToTestPage() {
        driver.navigate().to("about:blank");
        driver.navigate().to(url);
        testPage = pageFactory.get(TestPage.class);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
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
        Alert alert = driver.switchTo().alert();
        assertEquals("Double clicked!", alert.getText());
        alert.accept();
    }

    @Test
    public void canDragAndDrop() {
        testPage.drag.dropOnto(testPage.drop);

        Alert alert = driver.switchTo().alert();
        assertEquals("Dropped!", alert.getText());
        alert.accept();
    }

    @Test
    public void canInstantiatePageWhenBrowserDoesNotHaveRequestedElements() {
        driver.navigate().to("about:blank");
        testPage = pageFactory.get(TestPage.class);
    }

    @Test
    public void waitsForElement() throws TimeoutException {
        testPage.createElementAfterDelay.click();
        testPage.newElement.waitFor();

        assertEquals("Hello World", testPage.newElement.getText());
    }

    @Test(expected = TimeoutException.class)
    public void timesOutWaitingForAnElement() throws TimeoutException {
        testPage.createElementAfterDelay.click();
        testPage.newElement.waitFor(SHORT_TIMEOUT);
    }

    @Test
    public void waitsForAnElementToBeGone() throws TimeoutException {
        testPage.removeElementAfterDelay.click();
        testPage.headline.waitUntilGone();
    }

    @Test(expected = TimeoutException.class)
    public void timesOutWaitingForAnElementToBeGone() throws TimeoutException {
        testPage.removeElementAfterDelay.click();

        testPage.headline.waitUntilGone(SHORT_TIMEOUT);
    }

    @Test
    public void waitsforElementToBeHidden() throws TimeoutException {
        testPage.hideElementAfterDelay.click();
        testPage.headline.waitUntilHidden();
    }

    @Test(expected = TimeoutException.class)
    public void timesOutWhileWaitingforElementToBeHidden() throws TimeoutException {
        testPage.hideElementAfterDelay.click();
        testPage.headline.waitUntilHidden(SHORT_TIMEOUT);
    }

    @Test
    public void waitForElementToBecomeVisible() throws TimeoutException {
        testPage.showElementAfterDelay.click();
        testPage.subtitle.waitUntilVisible();
    }

    @Test(expected = TimeoutException.class)
    public void timesOutWhileWaitingForElementToBecomeVisible() throws TimeoutException {
        testPage.showElementAfterDelay.click();
        testPage.subtitle.waitUntilVisible(SHORT_TIMEOUT);
    }


    @Test
    public void mapsPageSectionCorrectly() {
        assertNotNull("Page section should not be null", testPage.parent);
        assertNotNull("Page section should not be null", testPage.parent.child);
    }

    @Test
    public void canGetPageSection() throws Exception {
        assertEquals("There should only be 1 child element", 1, testPage.parent.child.children.size());
    }
    @Test
    public void returnsAListOfElements() throws Exception {
        assertEquals("There should only be 1 child element", 6, testPage.listPageElements.size());
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
}
