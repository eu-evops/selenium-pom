package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class WebElementHandler implements InvocationHandler {
    private WebElement webElement;
    private WebDriver driver;
    private SearchContext searchContext;
    private By by;
    private WebElement frame;

    public WebElementHandler(WebDriver driver, SearchContext searchContext, By by, WebElement webElement) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
        this.webElement = webElement;
    }

    public WebElementHandler(WebDriver driver, SearchContext searchContext, WebElement webElement) {
        this(driver, searchContext, null, webElement);
    }

    public WebElementHandler(WebDriver driver, SearchContext searchContext, By by) {
        this(driver, searchContext, by, null);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(this.webElement == null) {
            this.webElement = new PageElementImpl(driver, searchContext.findElement(this.by));
        }

        try {
            return method.invoke(this.webElement, args);
        } catch(InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
