package uk.sponte.automation.seleniumpom.helpers.sauce;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.remote.RemoteWebDriver;
import uk.sponte.automation.seleniumpom.tests.ui.BritishAirwaysTest;

public class SauceTestRule extends SauceOnDemandTestWatcher {
    private SessionAndDriverProvider sessionIdProvider;

    private SauceOnDemandAuthentication authentication;

    private boolean verboseMode;

    public SauceTestRule(SessionAndDriverProvider sessionIdProvider) {
        this(sessionIdProvider, new SauceOnDemandAuthentication(), false);
    }

    public SauceTestRule(SessionAndDriverProvider sessionIdProvider, SauceOnDemandAuthentication authentication) {
        this(sessionIdProvider, authentication, false);
    }

    public SauceTestRule(SessionAndDriverProvider sessionIdProvider, SauceOnDemandAuthentication authentication,
            boolean verboseMode) {
        super(sessionIdProvider, authentication, verboseMode);
        this.sessionIdProvider = sessionIdProvider;
        this.authentication = authentication;
        this.verboseMode = verboseMode;
    }

    public SauceTestRule(BritishAirwaysTest sessionIdProvider, boolean verbose) {
        this(sessionIdProvider, new SauceOnDemandAuthentication(), verbose);
    }

    @Override
    protected void starting(Description description) {
        System.setProperty("selenium.webdriver.remote.name", description.getDisplayName());
        super.starting(description);
    }

    @Override
    protected void succeeded(Description description) {
        if(runsOnSauce()) {
            super.succeeded(description);
        }
    }

    @Override
    protected void failed(Throwable e, Description description) {
        if(runsOnSauce()) {
            super.failed(e, description);
        }
    }

    private boolean runsOnSauce() {
        if(this.sessionIdProvider != null &&
                !RemoteWebDriver.class.isAssignableFrom(this.sessionIdProvider.getDriver().getClass())) {
            return false;
        }

        return System.getProperty("selenium.webdriver", "").equalsIgnoreCase("remote") &&
            System.getProperty("selenium.webdriver.remote.server", "").contains("://ondemand.saucelabs.com");
    }
}
