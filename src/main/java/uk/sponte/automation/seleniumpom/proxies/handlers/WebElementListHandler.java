package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;

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
    private WebDriver driver;
    private SearchContext searchContext;
    private By by;

    public WebElementListHandler(WebDriver driver, SearchContext searchContext, By by) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elements = searchContext.findElements(this.by);
        ArrayList<PageElement> pageElements = new ArrayList<PageElement>();

        for (WebElement webElement : elements) {
            WebElementHandler handler = new WebElementHandler(driver, searchContext, webElement);
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
