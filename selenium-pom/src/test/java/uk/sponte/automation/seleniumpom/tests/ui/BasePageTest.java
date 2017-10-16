package uk.sponte.automation.seleniumpom.tests.ui;

import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;


/**
 * Created by n450777 on 30/04/15.
 * BaseClass for UI tests
 */
public class BasePageTest {
    static final int SHORT_TIMEOUT = 200;
    static WebDriver driver;
    static PageFactory pageFactory;

    TestPage testPage;

    String getTestPagePath() {
        return "test.page.html";
    }

    private Options opts = new WireMockConfiguration()
            .dynamicPort()
            .disableRequestJournal()
            .withRootDirectory("src/test/resources/uk/sponte/automation/seleniumpom");

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(opts); // No-args constructor defaults to port 8080

    @BeforeClass
    public static void setup() {
        pageFactory = new PageFactory();
        driver = pageFactory.getDriver();
    }

    @Before
    public void navigateToTestPage() {
        String testUrl = wireMockRule.url(getTestPagePath());
        driver.navigate().to("about:blank");
        driver.navigate().to(testUrl);
        driver.manage().deleteAllCookies();
        testPage = pageFactory.get(TestPage.class);
    }

    @AfterClass
    public static void tearDown() {
        if(driver != null) {
            driver.quit();
        }
    }
}

