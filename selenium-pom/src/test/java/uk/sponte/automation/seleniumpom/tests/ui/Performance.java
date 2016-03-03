package uk.sponte.automation.seleniumpom.tests.ui;

import junit.framework.Assert;
import org.junit.Test;
import uk.sponte.automation.seleniumpom.PageElement;

/**
 * Created by n450777 on 03/03/2016.
 */
public class Performance extends BasePageTest {
    @Test
    public void canAccessLongList() {
        Assert.assertEquals(400, testPage.longListElements.size());

        for (PageElement pageElement : testPage.longListElements) {
            pageElement.getText();
        }
    }
}
