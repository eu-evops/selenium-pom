package uk.sponte.automation.seleniumpom.helpers.sauce;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import org.openqa.selenium.WebDriver;

public interface SessionAndDriverProvider extends SauceOnDemandSessionIdProvider {
    WebDriver getDriver();
}
