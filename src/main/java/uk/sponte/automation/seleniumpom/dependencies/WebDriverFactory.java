package uk.sponte.automation.seleniumpom.dependencies;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Created by n450777 on 07/04/15.
 */
public class WebDriverFactory implements DependencyFactory<WebDriver> {

    private WebDriver driver;

    @Override
    public WebDriver get() {
        if(driver != null) return driver;
        driver = createNewDriver();

        return driver;
    }

    private WebDriver createNewDriver() {
        String webdriverProperty = System.getProperty("selenium.webdriver", "firefox");

        if(webdriverProperty.equalsIgnoreCase("chrome"))
            return new ChromeDriver();

        if(webdriverProperty.equalsIgnoreCase("ie") ||
            webdriverProperty.equalsIgnoreCase("iexplore") ||
            webdriverProperty.equalsIgnoreCase("internet explorer"))
            return new InternetExplorerDriver();

        return new FirefoxDriver();
    }
}
