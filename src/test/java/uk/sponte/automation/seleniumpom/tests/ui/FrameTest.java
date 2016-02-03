package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Test;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.testobjects.pages.PageInsideFrame;
import uk.sponte.automation.seleniumpom.testobjects.pages.SimplePage;

import static org.junit.Assert.assertEquals;

/**
 * Created by n450777 on 30/04/15.
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
    public void canAccessElementsOnAPageInsideFrame() {
        PageInsideFrame pageInsideFrame = pageFactory.get(PageInsideFrame.class);

        System.out.println(pageInsideFrame.headline.getText());

        assertEquals("Inside a frame", pageInsideFrame.headline.getText());

        assertEquals("Section title", pageInsideFrame.frameSubSection.title.getText());

        PageElement pageElement = pageInsideFrame.frameSubSectionWithList.items.get(0);
        assertEquals("one", pageElement.getText());
    }
}
