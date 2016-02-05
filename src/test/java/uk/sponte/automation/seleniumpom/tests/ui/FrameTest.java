package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Test;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.testobjects.pages.PageInsideFrame;
import uk.sponte.automation.seleniumpom.testobjects.sections.Result;

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
}
