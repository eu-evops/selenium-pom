package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by n450777 on 07/04/15.
 */
public class ElementListImpl {

    public ElementListImpl(
            SearchContext searchContext,
            List<WebElement> webElementListProxy) {
    }

    public boolean canHandle(Method method) {
        return false;
    }
}
