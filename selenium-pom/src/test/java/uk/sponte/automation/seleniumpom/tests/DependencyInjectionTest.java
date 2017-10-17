package uk.sponte.automation.seleniumpom.tests;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.GuiceDependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.InjectionError;
import uk.sponte.automation.seleniumpom.helpers.TestDiInstance;
import uk.sponte.automation.seleniumpom.helpers.TestDiInstanceFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by n450777 on 10/02/2016.
 */
public class DependencyInjectionTest {

    private GuiceDependencyInjector di;

    @Before
    public void setupDi() {
        di = new GuiceDependencyInjector(null);
    }

    @Test
    public void simpleTypeReturnedFromDiMechanism() {
        di.registerFactory(new TestDiInstanceFactory());
        TestDiInstance testDiInstance = di.get(TestDiInstance.class);
        assertEquals("This instance is called 'secret'", testDiInstance.toString());
    }

    @Test
    public void canUseMyOwnDependencyInjection() {
        WebDriver webDriver = mock(WebDriver.class);
        PageFactory pageFactory = new PageFactory(new MyCustomGuiceInjector(webDriver));
        EventFiringWebDriver driver = (EventFiringWebDriver) pageFactory.getDriver();
        assertEquals(webDriver, driver.getWrappedDriver());
        webDriver.quit();
    }


    class MyCustomGuiceInjector extends AbstractModule
            implements DependencyInjector {

        private Injector injector;

        private WebDriver webDriver;

        private MyCustomGuiceInjector(
                WebDriver webDriver) {
            this.webDriver = webDriver;
        }

        @Override
        protected void configure() {
            bind(WebDriver.class).toInstance(webDriver);
        }

        @Override
        public <T> T get(Class<T> klass) throws InjectionError {
            return getInjector().getInstance(klass);
        }

        private Injector getInjector() {
            if(injector != null) return injector;
            injector = Guice.createInjector(Stage.PRODUCTION, this);
            return injector;
        }
    }
}