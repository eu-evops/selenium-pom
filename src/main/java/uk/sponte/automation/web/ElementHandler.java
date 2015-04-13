package uk.sponte.automation.web;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class ElementHandler implements InvocationHandler {
    private WebElement element;
    private WebElementExtensionsImpl extensionsHandler;

    public ElementHandler(WebElement element, WebElementExtensionsImpl extensionsHandler) {
        this.element = element;
        this.extensionsHandler = extensionsHandler;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (extensionsHandler.canHandle(method))
                return method.invoke(extensionsHandler, args);

            return method.invoke(this.element, args);
        } catch(InvocationTargetException exception) {
            throw exception.getCause();
        }
    }
}
