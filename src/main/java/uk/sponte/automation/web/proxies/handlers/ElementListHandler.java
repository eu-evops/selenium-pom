package uk.sponte.automation.web.proxies.handlers;

import org.openqa.selenium.WebElement;
import uk.sponte.automation.web.ElementListImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by swozniak on 03/04/15.
 */
public class ElementListHandler implements InvocationHandler {
    private List<WebElement> elementList;
    private ElementListImpl extensionsListHandler;

    public ElementListHandler(List<WebElement> element, ElementListImpl extensionsHandler) {
        this.elementList = element;
        this.extensionsListHandler = extensionsHandler;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (extensionsListHandler.canHandle(method))
                return method.invoke(extensionsListHandler, args);

            return method.invoke(this.elementList, args);
        } catch(InvocationTargetException exception) {
            throw exception.getCause();
        }
    }
}
