package uk.sponte.automation.web.tests;

import org.junit.Before;
import org.junit.Test;
import uk.sponte.automation.web.dependencies.DefaultDependencyInjectorImpl;
import uk.sponte.automation.web.helpers.TestDiInstance;
import uk.sponte.automation.web.helpers.TestDiInstanceFactory;

import java.util.ArrayList;
import java.util.Date;

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
