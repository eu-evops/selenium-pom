package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.sponte.automation.seleniumpom.testobjects.pages.Homepage;
import uk.sponte.automation.seleniumpom.testobjects.pages.PrettyHomepageImpl;
import uk.sponte.automation.seleniumpom.testobjects.pages.ZPrettyAndMobileHomepageImpl;

/**
 * Unit tests for PageFilters
 */
public class DifferentModelImplementationTests extends BaseMockTest {

    @Before
    public void resetSystemProperties() {
        System.clearProperty("Mobile");
        System.clearProperty("Pretty");
    }

    @Test
    public void pageWillReturnDefaultImplementation() {
        Homepage homepage = pageFactory.get(Homepage.class);
        Assert.assertEquals(homepage.getClass(), Homepage.class);
    }

    @Test
    public void pageFactoryWillReturnPrettyImplementationBasedOnAnnotation() {
        System.setProperty("Pretty", "true");

        Homepage homepage = pageFactory.get(Homepage.class);
        Assert.assertEquals(homepage.getClass(), PrettyHomepageImpl.class);
    }

    @Test
    public void pageFactoryWillReturnImplementationWithMostValidPageFilters() {
        System.setProperty("Pretty", "true");
        System.setProperty("Mobile", "true");

        Homepage homepage = pageFactory.get(Homepage.class);
        Assert.assertEquals(homepage.getClass(), ZPrettyAndMobileHomepageImpl.class);
    }
}
