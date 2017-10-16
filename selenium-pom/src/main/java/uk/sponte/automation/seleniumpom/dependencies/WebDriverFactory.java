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

import java.util.Properties;
import java.util.Set;

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

            Properties systemProperties = System.getProperties();
            Set<String> propertyNames = systemProperties.stringPropertyNames();

            for (String propertyName : propertyNames) {
                String property = systemProperties.getProperty(propertyName);

                if (propertyName.matches("selenium.webdriver.remote..*")) {
                    String capabilityName = propertyName
                            .replace("selenium.webdriver.remote.", "");
                    if (capabilityName.equalsIgnoreCase("server")) {
                        continue;
                    }

                    capabilities
                            .setCapability(capabilityName, property);
                }
            }

            // Sauce support
            if(System.getenv("SAUCE_USERNAME") != null &&
                    System.getenv("SAUCE_ACCESS_KEY") != null) {
                capabilities.setCapability("username", System.getenv("SAUCE_USERNAME"));
                capabilities.setCapability("access-key", System.getenv("SAUCE_ACCESS_KEY"));
            }

            if(System.getenv("TUNNEL_IDENTIFIER") != null) {
                capabilities.setCapability("tunnelIdentifier", System.getenv("TUNNEL_IDENTIFIER"));
            }

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
