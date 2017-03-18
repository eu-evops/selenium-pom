package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class
 * Created by n450777 on 17/03/2017.
 */
public class StaleElementTests extends BasePageTest {
    @Test
    public void automaticallyRefreshesModelAfterStaleElementException() {
        String paragraphsText = testPage.staleElementsSection.getParagraphsText();
        testPage.staleElementsSection.triggerStaleElement();
        Assert.assertEquals(paragraphsText, testPage.staleElementsSection.getParagraphsText());
    }
}
