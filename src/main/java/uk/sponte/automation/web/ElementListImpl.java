package uk.sponte.automation.web;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by n450777 on 07/04/15.
 */
public class ElementListImpl {
    private final List<WebElement> webElementListProxy;
    private SearchContext searchContext;

    public ElementListImpl(
            SearchContext searchContext,
            List<WebElement> webElementListProxy) {
        this.searchContext = searchContext;
        this.webElementListProxy = webElementListProxy;
    }

    public boolean canHandle(Method method) {
        return false;
    }
}
