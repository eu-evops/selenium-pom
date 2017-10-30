package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.Test;
import uk.sponte.automation.seleniumpom.testobjects.pages.ITestPage;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;

import static org.junit.Assert.assertEquals;

public class PageInterfacesTests extends BaseMockTest {

    @Test
    public void canUsePageInterfacesToGetThePage() {
        ITestPage testPage = pageFactory.get(ITestPage.class);
        assertEquals(testPage.getClass(), TestPage.class);
    }
}
