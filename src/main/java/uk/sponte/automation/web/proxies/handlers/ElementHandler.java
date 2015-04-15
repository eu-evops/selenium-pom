package uk.sponte.automation.web.proxies.handlers;

import org.openqa.selenium.WebElement;
import uk.sponte.automation.web.PageElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class ElementHandler implements InvocationHandler {
    private WebElement element;
    private PageElement pageElement;

    public ElementHandler(WebElement element, PageElement pageElement) {
        this.element = element;
        this.pageElement = pageElement;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (pageElement.canHandle(method)) {
                return method.invoke(pageElement, args);
            }

            return method.invoke(this.element, args);
        } catch(InvocationTargetException exception) {
            throw exception.getCause();
        }
    }
}
