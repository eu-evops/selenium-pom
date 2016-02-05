package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.testobjects.pages.PageInsideFrame;
import uk.sponte.automation.seleniumpom.testobjects.sections.Result;

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
        System.out.println("Before getting the title object");
        PageElement title = testPage.iframe.innerFrame.innerInnerFrameSection.title;
        System.out.println("Before printing");
        // System.out.println(title.getText());
        System.out.println("Before clicking");
        title.click();
        System.out.println("After clicking");
        System.out.println(testPage.iframe.innerFrame.innerInnerFrameSection.newElement.getTagName());
        assertEquals("Hello World", testPage.iframe.innerFrame.innerInnerFrameSection.clickAndWaitForNewContent().getText());
    }

    @Test
    public void standardSeleniumTest() throws InterruptedException {
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));

        WebElement buttonInAFrame = driver.findElement(By.id("createElementAfterDelay"));

        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));

        buttonInAFrame.click();

        Thread.sleep(100);

        WebElement textElement = driver.findElement(By.id("newElement"));

        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));

        System.out.println(textElement.getText());
    }
}
