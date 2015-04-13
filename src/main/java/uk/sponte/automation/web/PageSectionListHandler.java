package uk.sponte.automation.web;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by n450777 on 08/04/15.
 */
public class PageSectionListHandler implements InvocationHandler {
    private SearchContext searchContext;
    private By by;
    private Class pageSectionType;
    private PageFactory pageFactory;

    public PageSectionListHandler(SearchContext searchContext, By by, Class pageSectionType, PageFactory pageFactory) {
        this.searchContext = searchContext;
        this.by = by;
        this.pageSectionType = pageSectionType;
        this.pageFactory = pageFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elements = searchContext.findElements(by);

        List pageSections = new ArrayList();
        for (WebElement element : elements) {
            pageSections.add(pageFactory.get(pageSectionType, element));
        }

        return method.invoke(pageSections, args);
    }
}
