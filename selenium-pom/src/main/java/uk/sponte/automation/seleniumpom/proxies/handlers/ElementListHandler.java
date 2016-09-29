package uk.sponte.automation.seleniumpom.proxies.handlers;

import uk.sponte.automation.seleniumpom.ElementListImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by swozniak on 03/04/15.
 */
public class ElementListHandler implements InvocationHandler {
    private List elementList;
    private ElementListImpl extensionsListHandler;

    public ElementListHandler(List element, ElementListImpl extensionsHandler) {
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
