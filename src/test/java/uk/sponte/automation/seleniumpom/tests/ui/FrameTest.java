package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by n450777 on 30/04/15.
 */
public class FrameTest extends BasePageTest {

    @Test
    public void canAccessContentInsideAFrame() {
        assertEquals("Inside a frame", testPage.iframe.headline.getText());
    }

    @Test
    public void canAccessContentInsideAndOutsideOfTheFrame() {
        assertEquals("Headline", testPage.headline.getText());
        canAccessContentInsideAFrame();
        assertEquals("Headline", testPage.headline.getText());
    }

    @Test
    public void canAccessSubSectionInsideAFrame() {
        assertEquals("Section title", testPage.iframe.frameSubSection.title.getText());
    }
}
