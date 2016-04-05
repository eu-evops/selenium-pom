package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.helpers.TestHelper;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;

/**
 * Created by n450777 on 30/04/15.
 * BaseClass for UI tests
 */
public class BasePageTest {
    static final int SHORT_TIMEOUT = 200;
    static WebDriver driver;
    static PageFactory pageFactory;
    static TestHelper testHelper = new TestHelper();

    TestPage testPage;

    String getTestPagePath() {
        return "test.page.html";
    }

    @BeforeClass
    public static void setup() {

        pageFactory = new PageFactory();
        driver = pageFactory.getDriver();
    }

    @Before
    public void navigateToTestPage() {
        String url = testHelper.getTestPageAsBase64(getTestPagePath());
        driver.navigate().to("about:blank");
        driver.navigate().to(url);
        testPage = pageFactory.get(TestPage.class);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}

