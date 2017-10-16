package uk.sponte.automation.seleniumpom.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.dependencies.WebDriverFactory;

/**
 * Created by n450777 on 30/11/2015.
 */
public class DependencyInjectionConfiguration extends AbstractModule {
    @Override
    protected void configure() {
        bind(WebDriver.class)
            .toProvider(WebDriverFactory.class)
            .in(Singleton.class);
    }
}
