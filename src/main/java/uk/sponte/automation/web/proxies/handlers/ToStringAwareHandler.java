package uk.sponte.automation.web.proxies.handlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by n450777 on 13/04/15.
 */
public abstract class ToStringAwareHandler implements InvocationHandler {
    private static final Method OBJECT_TO_STRING = getObjectMethod("toString", Object.class);

    public String toString() {
        return String.format("[%s] %s", hashCode(), getClass());
    }

    private static Method getObjectMethod(String name, Class... types) {
        try {
            // null 'types' is OK.
            return Object.class.getMethod(name, types);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
