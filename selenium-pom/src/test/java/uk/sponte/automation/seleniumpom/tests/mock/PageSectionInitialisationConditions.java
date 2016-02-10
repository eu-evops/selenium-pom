package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by n450777 on 01/05/15.
 * PageConditions tests
 */
public class PageSectionInitialisationConditions extends BaseMockTest {

    @Test
    public void basedOnSectionAnnotation() {
        assertNotNull(testPage.parent);
    }

    @Test
    public void basedOnFindByAnnotation() {
        assertNotNull(testPage.basedOnFindByAnnotation);
    }

    @Test
    public void basedOnFieldType() {
        assertNotNull(testPage.section);
    }
}
