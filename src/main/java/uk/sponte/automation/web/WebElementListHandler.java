package uk.sponte.automation.web;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swozniak on 03/04/15.
 */
public class WebElementListHandler implements InvocationHandler {
    private SearchContext driver;
    private By by;

    public WebElementListHandler(SearchContext driver, By by) {
        this.driver = driver;
        this.by = by;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elements = driver.findElements(this.by);
        ArrayList<PageElement> pageElements = new ArrayList<PageElement>();

        for (WebElement webElement : elements) {
            PageElementHandler handler = new PageElementHandler(webElement);
            PageElement pageElement = (PageElement) Proxy.newProxyInstance(PageElement.class.getClassLoader(),
                    new Class[]{PageElement.class},
                    handler);

            pageElements.add(pageElement);
        }

        try {
            return method.invoke(pageElements, args);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
