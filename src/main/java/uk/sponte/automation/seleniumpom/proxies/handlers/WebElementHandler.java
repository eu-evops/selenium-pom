package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class WebElementHandler implements InvocationHandler {
    private SearchContext searchContext;
    private By by;

    public WebElementHandler(SearchContext searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        WebElement element = searchContext.findElement(this.by);
        try {
            return method.invoke(element, args);
        } catch(InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
