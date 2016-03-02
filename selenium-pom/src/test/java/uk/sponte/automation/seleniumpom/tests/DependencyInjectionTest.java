package uk.sponte.automation.seleniumpom.tests;

import org.junit.Before;
import org.junit.Test;
import uk.sponte.automation.seleniumpom.dependencies.GuiceDependencyInjector;
import uk.sponte.automation.seleniumpom.helpers.TestDiInstance;
import uk.sponte.automation.seleniumpom.helpers.TestDiInstanceFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by n450777 on 10/02/2016.
 */
public class DependencyInjectionTest {

    private GuiceDependencyInjector di;

    @Before
    public void setupDi() {
        di = new GuiceDependencyInjector();
    }

    @Test
    public void simpleTypeReturnedFromDiMechanism() {
        di.registerFactory(new TestDiInstanceFactory());
        TestDiInstance testDiInstance = di.get(TestDiInstance.class);
        assertEquals("This instance is called 'secret'", testDiInstance.toString());
    }
}