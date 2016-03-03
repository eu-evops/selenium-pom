package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by n450777 on 01/05/15.
 */
public class PageElementInitialisationConditions extends BaseMockTest {
    @Test
    public void pageCreation() {
        assertNotNull("page has not been initialised", testPage);
    }

    @Test
    public void basedOnFindByAnnotation() {
        assertNotNull("pageElement has not been initialized", testPage.headline);
    }

    @Test
    public void basedOnPageElementType() {
        assertNotNull(testPage.drag);
    }

}
