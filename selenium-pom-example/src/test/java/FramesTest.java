import com.google.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.guice.DependencyInjectionConfiguration;
import uk.sponte.automation.seleniumpom.guice.SeleniumPomGuiceModule;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

/**
 * Created by n450777 on 30/11/2015.
 */
public class FramesTest {
    @Inject private pages.w3schools.PageWithAFrame pageWithAFrame;
    @Inject private WebDriver driver;

    @Before
    public void setupDependencyInjection() {
        SeleniumPomGuiceModule seleniumPomGuiceModule = new SeleniumPomGuiceModule(new DependencyInjectionConfiguration());
        seleniumPomGuiceModule.injectMembers(this);

        driver.navigate().to("http://www.w3schools.com/html/html_iframe.asp");
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void runExampleTest() throws TimeoutException {
        String titleText = pageWithAFrame.frameContent.title.getText();
        assertEquals("Examples in Every Chapter", titleText);
    }
}
