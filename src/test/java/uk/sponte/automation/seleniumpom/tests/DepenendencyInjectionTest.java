package uk.sponte.automation.seleniumpom.tests;

import org.junit.Before;
import org.junit.Test;
import uk.sponte.automation.seleniumpom.dependencies.DefaultDependencyInjectorImpl;
import uk.sponte.automation.seleniumpom.helpers.TestDiInstance;
import uk.sponte.automation.seleniumpom.helpers.TestDiInstanceFactory;

import static junit.framework.Assert.assertEquals;

/**
 * Created by n450777 on 07/04/15.
 */
public class DepenendencyInjectionTest {

    private DefaultDependencyInjectorImpl di;

    @Before
    public void setupDi() {
        di = new DefaultDependencyInjectorImpl();
    }

    @Test
    public void simpleTypeReturnedFromDiMechanism() {
        di.registerFactory(TestDiInstance.class, new TestDiInstanceFactory());
        TestDiInstance testDiInstance = di.get(TestDiInstance.class);
        assertEquals("This instance is called 'secret'", testDiInstance.toString());
    }
}
