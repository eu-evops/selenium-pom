package uk.sponte.automation.seleniumpom.dependencies;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by n450777 on 07/04/15.
 */
public class WebDriverFactory implements DependencyFactory {

    private WebDriver driver;

    @Override
    public WebDriver get() {
        if(driver != null) return driver;
        driver = createNewDriver();

        return driver;
    }

    private WebDriver createNewDriver() {
        String webdriverProperty = System.getProperty("selenium.webdriver");

        if(webdriverProperty == null)
            return new HtmlUnitDriver(DesiredCapabilities.chrome());

        if(webdriverProperty.equalsIgnoreCase("chrome"))
            return new ChromeDriver();

        if(webdriverProperty.equalsIgnoreCase("firefox"))
            return new FirefoxDriver();

        if(webdriverProperty.equalsIgnoreCase("htmlunit"))
            return new HtmlUnitDriver(BrowserVersion.FIREFOX_24);

        return null;
    }
}
