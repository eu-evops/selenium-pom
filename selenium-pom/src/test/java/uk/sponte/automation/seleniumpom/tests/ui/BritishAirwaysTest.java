package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.testobjects.ba.MobileDateSelector;
import uk.sponte.automation.seleniumpom.testobjects.ba.BaMobileHomePage;

import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * Created by n450777 on 04/03/2016.
 */
public class BritishAirwaysTest {
    PageFactory pageFactory;
    BaMobileHomePage baMobileHomePage;
    MobileDateSelector mobileDateSelector;

    @Before
    public void setup() {
        System.setProperty("selenium.webdriver", "chrome");
        pageFactory = new PageFactory();
        baMobileHomePage = pageFactory.get(BaMobileHomePage.class);
        mobileDateSelector = pageFactory.get(MobileDateSelector.class);
    }

    @After
    public void closeBrowser() {
        pageFactory.getDriver().quit();
    }

    @Test
    public void useOfCalendars() throws TimeoutException {
        baMobileHomePage.load();
        baMobileHomePage.departureDate.click();

        Calendar cal = Calendar.getInstance();
        cal.set(2016, 11, 24);
        mobileDateSelector.setDeparture(cal.getTime());

        baMobileHomePage.returnDate.click();
        cal.add(Calendar.DAY_OF_MONTH, 20);
        mobileDateSelector.setReturn(cal.getTime());
    }

}
