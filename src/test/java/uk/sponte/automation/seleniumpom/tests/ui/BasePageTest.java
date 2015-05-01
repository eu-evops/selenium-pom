package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DefaultDependencyInjectorImpl;
import uk.sponte.automation.seleniumpom.helpers.TestHelper;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by n450777 on 30/04/15.
 */
public class BasePageTest {
    protected static final int SHORT_TIMEOUT = 200;
    protected static WebDriver driver;
    protected static PageFactory pageFactory;
    protected static String url;
    protected TestPage testPage;

    @BeforeClass
    public static void setup() {
        System.setProperty("selenium.webdriver", "chrome");
        Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);


        TestHelper testHelper = new TestHelper();
        url = testHelper.getTestPageAsBase64();

        DefaultDependencyInjectorImpl defaultDependencyInjector = new DefaultDependencyInjectorImpl();

        driver = defaultDependencyInjector.get(WebDriver.class);
        pageFactory = new PageFactory(defaultDependencyInjector);
    }

    @Before
    public void navigateToTestPage() {
        driver.navigate().to("about:blank");
        driver.navigate().to(url);
        testPage = pageFactory.get(TestPage.class);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
