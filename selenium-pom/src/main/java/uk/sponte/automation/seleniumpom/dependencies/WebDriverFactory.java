package uk.sponte.automation.seleniumpom.dependencies;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.function.BiConsumer;

/**
 * Created by n450777 on 07/04/15.
 */
@Singleton
public class WebDriverFactory
        implements DependencyFactory<WebDriver>, Provider<WebDriver> {

    private WebDriver driver;

    @Override
    public WebDriver get() {
        if (driver != null)
            return driver;
        driver = createNewDriver();

        return driver;
    }

    private WebDriver createNewDriver() {
        String webdriverProperty = System
                .getProperty("selenium.webdriver", "chrome");
        String webDriverServer = System
                .getProperty("selenium.webdriver.remote.server", null);

        if (webDriverServer != null) {
            System.setProperty("webdriver.remote.server",
                    webDriverServer);
        }

        if (webdriverProperty.equalsIgnoreCase("remote")) {
            final DesiredCapabilities capabilities = new DesiredCapabilities();
            System.getProperties().forEach(new BiConsumer<Object, Object>() {
                @Override
                public void accept(Object o, Object o2) {
                    if (o.toString().matches("selenium.webdriver.remote..*")) {
                        String capabilityName = o.toString()
                                .replace("selenium.webdriver.remote.", "");
                        capabilities
                                .setCapability(capabilityName, o2.toString());
                    }
                }
            });
            return new RemoteWebDriver(capabilities);
        }

        if (webdriverProperty.equalsIgnoreCase("chrome"))
            return new ChromeDriver();

        if (webdriverProperty.equalsIgnoreCase("ie") ||
                webdriverProperty.equalsIgnoreCase("iexplore") ||
                webdriverProperty.equalsIgnoreCase("internet explorer"))
            return new InternetExplorerDriver();

        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setEnableNativeEvents(false);
        return new FirefoxDriver(firefoxProfile);
    }
}
