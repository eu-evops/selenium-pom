package uk.sponte.automation.seleniumpom.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.Stage;
import com.google.inject.matcher.Matchers;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.InjectionError;

/**
 * Created by n450777 on 30/11/2015.
 */
public class DependencyInjectionConfiguration extends AbstractModule {
    @Override
    protected void configure() {
        bind(WebDriver.class)
            .toProvider(WebDriverProvider.class)
            .in(Singleton.class);
    }
}
