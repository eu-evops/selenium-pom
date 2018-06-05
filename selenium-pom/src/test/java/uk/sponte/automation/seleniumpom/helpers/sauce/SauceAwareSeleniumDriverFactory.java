package uk.sponte.automation.seleniumpom.helpers.sauce;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import uk.sponte.automation.seleniumpom.dependencies.DependencyFactory;

import static java.lang.String.format;

/**
 * Created by n450777 on 26/09/2016.
 */
public class SauceAwareSeleniumDriverFactory implements DependencyFactory<WebDriver> {
    @Override
    public WebDriver get() {
        System.setProperty("webdriver.remote.server", getRemoteHubUrl());

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.BROWSER_NAME, getEnv("SELENIUM_BROWSER"));
        caps.setCapability(CapabilityType.VERSION, getEnv("SELENIUM_VERSION"));
        caps.setCapability("username", getEnv("SAUCE_USERNAME"));
        caps.setCapability("access-key", getEnv("SAUCE_ACCESS_KEY"));

        return new RemoteWebDriver(caps);
    }

    private String getRemoteHubUrl() {
        String seleniumPort = getEnv("SELENIUM_PORT");
        String protocol = "http";

        if(seleniumPort.equals("443")) {
            protocol = "https";
        }

        return format("%s://%s:%s/wd/hub", protocol, getEnv("SELENIUM_HOST"), getEnv("SELENIUM_PORT"));
    }

    private String getEnv(String selenium_port) {
        return System.getenv(selenium_port);
    }
}
