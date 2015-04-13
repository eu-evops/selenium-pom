package uk.sponte.automation.web;

import org.openqa.selenium.SearchContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by n450777 on 10/04/15.
 */
public class PageSectionHandler implements InvocationHandler {
    private PageSection pageSection;
    private SearchContext container;

    public PageSectionHandler(PageSection pageSection, SearchContext container) {
        this.pageSection = pageSection;
        this.container = container;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("isPresent")) {
            container.toString();
            return true;
        }

        return method.invoke(proxy, args);
    }
}
