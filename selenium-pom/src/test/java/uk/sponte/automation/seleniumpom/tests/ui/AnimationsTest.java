package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Before;
import org.junit.Test;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.testobjects.pages.AnimatedTestPage;

import static org.junit.Assert.assertEquals;

/**
 * Created by n450777 on 10/03/2016.
 */
public class AnimationsTest extends BasePageTest {

    private AnimatedTestPage animatedTestPage;

    @Override
    String getTestPagePath() {
        return "test.page.animations.html";
    }

    @Before
    public void setupTestCase() {
        animatedTestPage = pageFactory
                .get(AnimatedTestPage.class);
    }


    @Test
    public void waitsForElementToStopMoving() throws InterruptedException {
        PageElement thirdListItem = animatedTestPage.listElements.get(2);

        // this will cache element in selenium pom
        thirdListItem.getText();
        animatedTestPage.moveButton.click();
        thirdListItem.waitUntilStopsMoving();
        thirdListItem.click();
        Thread.sleep(100);
        assertEquals("Clicked!", thirdListItem.getText());
    }

}
