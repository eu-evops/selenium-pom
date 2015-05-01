package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class FrameHandler implements InvocationHandler {
    private SearchContext searchContext;
    private By by;
    private WebDriver driver;

    public FrameHandler(SearchContext searchContext, By by, WebDriver driver) {
        this.searchContext = searchContext;
        this.by = by;
        this.driver = driver;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        WebElement element = searchContext.findElement(this.by);
        try {
            System.out.printf("Calling method %s on frame%n", method);
            driver.switchTo().frame(element);
            System.out.printf("Switched to a frame: %s%n", element);
            result = method.invoke(driver.findElement(By.tagName("body")), args);
        } catch(InvocationTargetException ex) {
            throw ex.getCause();
        } finally {
            driver.switchTo().defaultContent();
            return result;
        }
    }
}
