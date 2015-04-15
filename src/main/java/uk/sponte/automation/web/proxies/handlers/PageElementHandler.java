package uk.sponte.automation.web.proxies.handlers;

import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by n450777 on 07/04/15.
 */
public class PageElementHandler implements InvocationHandler {


    private WebElement webElement;

    public PageElementHandler(WebElement webElement) {

        this.webElement = webElement;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(this.webElement, args);
    }
}
