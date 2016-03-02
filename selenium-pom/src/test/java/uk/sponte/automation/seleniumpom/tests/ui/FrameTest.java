package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.testobjects.pages.PageInsideFrame;
import uk.sponte.automation.seleniumpom.testobjects.sections.Result;

import java.lang.reflect.Field;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by n450777 on 30/04/15.
 * Test class
 */
public class FrameTest extends BasePageTest {

    @Test
    public void canAccessContentInsideAndOutsideOfTheFrame() {
        assertEquals("Headline", testPage.headline.getText());
        canAccessContentInsideAFrame();
        assertEquals("Headline", testPage.headline.getText());
    }

    @Test
    public void canAccessContentInsideAFrame() {
        assertEquals("Inside a frame", testPage.iframe.headline.getText());
    }


    @Test
    public void canAccessSubSectionInsideAFrame() {
        assertEquals("Section title", testPage.iframe.frameSubSection.title.getText());
    }

    @Test
    public void canAccessElementsWithinSectionsOfSections() {
        Result result = testPage.iframe.frameSubSection.resultSections.get(1).results.get(4);
        assertEquals("Section 2 - Result 5", result.title.getText());
    }

    @Test
    public void canAccessElementsWithinAFrameThatsInsideAnotherFrame() {
        String innerFrameSubtitle = testPage.iframe.innerFrame.innerInnerFrameSection.title.getText();

        assertEquals("Section title", innerFrameSubtitle);
    }

    @Test
    public void canAccessElementsOnAPageInsideFrame() {
        PageInsideFrame pageInsideFrame = pageFactory.get(PageInsideFrame.class);
        assertEquals("Section title", pageInsideFrame.innerFrameSection.innerInnerFrameSection.title.getText());

        assertEquals("Inside a frame", pageInsideFrame.headline.getText());
        assertEquals("Section title", pageInsideFrame.frameSubSection.title.getText());

        PageElement pageElement = pageInsideFrame.frameSubSectionWithList.items.get(0);
        assertEquals("one", pageElement.getText());
    }

    @Test
    public void canWaitForElementsInsideNestedFrames() throws TimeoutException {
        System.out.println(testPage.iframe.innerFrame.innerInnerFrameSection.title.getText());
        PageElement title = testPage.iframe.innerFrame.innerInnerFrameSection.title;
        System.out.println(title.getText());
        System.out.println(title.getText());
        title.click();
        System.out.println("After clicking");
        System.out.println(title.getText());
//        System.out.println(testPage.iframe.innerFrame.innerInnerFrameSection.newElement.getTagName());
//        assertEquals("Hello World", testPage.iframe.innerFrame.innerInnerFrameSection.clickAndWaitForNewContent().getText());
    }

    @Test
    public void standardSeleniumTest() throws InterruptedException {
        WebDriverFrameSwitchingOrchestrator orchestrator = dependencyInjector.get(WebDriverFrameSwitchingOrchestrator.class);

        FrameWrapper iframe1 = new FrameWrapper(driver, By.tagName("iframe"));
        FrameWrapper iframe2 = new FrameWrapper(driver, By.tagName("iframe"));
        FrameWrapper iframe3 = new FrameWrapper(driver, By.tagName("iframe"));
        iframe3.setParent(iframe2);
        iframe2.setParent(iframe1);

        orchestrator.useFrame(iframe3);
        WebElement buttonInAFrame = driver.findElement(By.id("createElementAfterDelay"));

        orchestrator.useFrame(null);
        orchestrator.useFrame(iframe3);

        buttonInAFrame.click();

        Thread.sleep(100);

        WebElement textElement = driver.findElement(By.id("newElement"));

        orchestrator.useFrame(null);
        orchestrator.useFrame(iframe3);

        System.out.println(textElement.getText());
    }
}
