package uk.sponte.automation.seleniumpom.tests.ui;

import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.helpers.sauce.SauceTestRule;
import uk.sponte.automation.seleniumpom.helpers.sauce.SessionAndDriverProvider;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by n450777 on 30/04/15.
 * BaseClass for UI tests
 */
public class BasePageTest implements SessionAndDriverProvider {
    static final int SHORT_TIMEOUT = 0;
    protected WebDriver driver;
    protected PageFactory pageFactory;

    protected TestPage testPage;

    private String sessionId;

    String getTestPagePath() {
        return "test.page.html";
    }

    private Options opts = new WireMockConfiguration()
            .dynamicPort()
            .disableRequestJournal()
            .withRootDirectory("src/test/resources/uk/sponte/automation/seleniumpom");

    @Rule
    public SauceTestRule sauceTestRule = new SauceTestRule(this);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(opts); // No-args constructor defaults to port 8080

    @Before
    public void navigateToTestPage() {
        pageFactory = new PageFactory();
        driver = pageFactory.getDriver();
        String testUrl = wireMockRule.url(getTestPagePath());
        driver.navigate().to("about:blank");
        driver.navigate().to(testUrl);
        driver.manage().deleteAllCookies();
        testPage = pageFactory.get(TestPage.class);
    }

    @After
    public void tearDown() {
        if(driver != null) {
            driver.quit();
        }
    }

    @Override
    public WebDriver getDriver() {
        return pageFactory.getRawDriver();
    }

    @Override
    public String getSessionId() {
        if(this.pageFactory == null) {
            return "";
        }

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

}

