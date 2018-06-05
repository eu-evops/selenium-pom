package uk.sponte.automation.seleniumpom.tests.ui;

import com.saucelabs.junit.SauceOnDemandTestWatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.helpers.sauce.SauceTestRule;
import uk.sponte.automation.seleniumpom.helpers.sauce.SessionAndDriverProvider;
import uk.sponte.automation.seleniumpom.testobjects.ba.BaMobileHomePage;
import uk.sponte.automation.seleniumpom.testobjects.ba.MobileDateSelector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * Created by n450777 on 04/03/2016.
 */
public class BritishAirwaysTest implements SessionAndDriverProvider {
    private PageFactory pageFactory;
    private BaMobileHomePage baMobileHomePage;
    private MobileDateSelector mobileDateSelector;

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceTestRule(this, true);

    private String sessionId;

    @Before
    public void setup() {
        pageFactory = new PageFactory();
        this.sessionId = getSessionId();
        baMobileHomePage = pageFactory.get(BaMobileHomePage.class);
    }

    @After
    public void closeBrowser() {
        pageFactory.getDriver().quit();
    }

    @Test
    public void useOfCalendars() throws TimeoutException {
        baMobileHomePage.load();
        baMobileHomePage.departureDate.waitFor().click();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 6);

        MobileDateSelector outboundCalendar = pageFactory.get(MobileDateSelector.class);
        outboundCalendar.setDeparture(cal.getTime());

        baMobileHomePage.returnDate.click();
        cal.add(Calendar.DAY_OF_MONTH, 20);
        MobileDateSelector returnCalendar = pageFactory.get(MobileDateSelector.class);
        returnCalendar.setReturn(cal.getTime());
    }

    @Override
    public String getSessionId() {
        if(this.sessionId != null) {
            return this.sessionId;
        }

        try {
            Method getSessionIdMethod = RemoteWebDriver.class
                    .getMethod("getSessionId");
            return getSessionIdMethod.invoke(pageFactory.getRawDriver())
                    .toString();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public WebDriver getDriver() {
        return pageFactory.getRawDriver();
    }
}
