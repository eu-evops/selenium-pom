package uk.sponte.automation.seleniumpom.proxies.handlers;

import java.lang.reflect.Method;

/**
 * Created by n450777 on 13/04/15.
 */
public interface DynamicHandler {
    boolean canHandle(Method method);
}
