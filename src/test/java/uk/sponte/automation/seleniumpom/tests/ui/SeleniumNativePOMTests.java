package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.support.PageFactory;
import uk.sponte.automation.seleniumpom.testobjects.pages.SeleniumTestPage;

/**
 * Created by n450777 on 27/11/2015.
 * Test for native selenium page objects
 */
public class SeleniumNativePOMTests extends BasePageTest {
    private SeleniumTestPage legacySeleniumPOM;
    private SeleniumTestPage seleniumPOM;

    @Before
    public void initializePages() {
        legacySeleniumPOM = PageFactory.initElements(driver, SeleniumTestPage.class);
        seleniumPOM = pageFactory.get(SeleniumTestPage.class);
    }

    @Test
    public void canUseSeleniumLegacyPOM() {
        Assert.assertEquals(
                legacySeleniumPOM.headline.getText(),
                seleniumPOM.headline.getText());
    }

    @Test
    public void canUseLegacyListsOfWebElement() {
        Assert.assertEquals(
                legacySeleniumPOM.listPageElements.size(),
                seleniumPOM.listPageElements.size());

        Assert.assertEquals(
                legacySeleniumPOM.listPageElements.get(4).getText(),
                seleniumPOM.listPageElements.get(4).getText()
        );
    }
}
