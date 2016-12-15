package uk.sponte.automation.seleniumpom.tests.mock;

import javassist.*;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.Page;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by n450777 on 30/10/2016.
 */
public class ExtractInformationFromElementsTest extends BaseMockTest {

    @Test
    public void canReadTextFromElements() {
        String expectedText = "Automatically extracted";
        when(headlineWebElementMock.getText()).thenReturn(expectedText);

        assertEquals(expectedText, testPage.automaticallyExtracted);

        assertEquals("There should be 3 list elements on the page", 3, testPage.listPageElements.size());
        assertEquals(expectedText, testPage.listPageElements.get(0).getText());
    }

    @Test
    public void testStringProxy() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        ClassPool aDefault = ClassPool.getDefault();
        CtClass page = aDefault.get(MyClass.class.getName());
        CtMethod toString = page.getDeclaredMethod("toString");
        toString.insertBefore("{ System.out.println(\"LOL\"); }");

        MyClass o = (MyClass)page.toClass().newInstance();

        System.out.println(o.toString());
    }

    class MyClass {
        public String myField = "initialValue";

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
