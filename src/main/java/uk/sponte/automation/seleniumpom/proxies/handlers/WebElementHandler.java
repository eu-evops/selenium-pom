package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class WebElementHandler implements InvocationHandler {
    private WebDriver driver;
    private SearchContext searchContext;
    private By by;

    public WebElementHandler(WebDriver driver, SearchContext searchContext, By by) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PageElement element = new PageElementImpl(driver, searchContext.findElement(this.by));

        try {
            return method.invoke(element, args);
        } catch(InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
