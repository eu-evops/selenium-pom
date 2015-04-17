package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.PageElementImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by n450777 on 08/04/15.
 */
public class PageSectionListHandler<T> implements InvocationHandler {
    private WebDriver driver;
    private SearchContext searchContext;
    private By by;
    private Class<T> pageSectionType;
    private PageFactory pageFactory;

    public PageSectionListHandler(
            WebDriver driver,
            SearchContext searchContext,
            By by,
            Class<T> pageSectionType,
            PageFactory pageFactory) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
        this.pageSectionType = pageSectionType;
        this.pageFactory = pageFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elements = searchContext.findElements(by);
        List<T> pageSections = new ArrayList<T>();
        for (WebElement element : elements) {
            PageElement webElementExtensions = new PageElementImpl(driver, element, null);

            InvocationHandler pageElementHandler = new ElementHandler(element, webElementExtensions);
            PageElement instance = (PageElement)Proxy.newProxyInstance(
                    PageElement.class.getClassLoader(),
                    new Class[]{PageElement.class},
                    pageElementHandler);

            T pageSection = pageFactory.get(pageSectionType, instance);
            pageSections.add(pageSection);
        }

        return method.invoke(pageSections, args);
    }
}
