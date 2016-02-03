package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import uk.sponte.automation.seleniumpom.PageElementImpl;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class PageElementHandler implements InvocationHandler {
    private PageElementImpl pageElement;
    private By frame;
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;

    public PageElementHandler(PageElementImpl pageElement, By frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        this.pageElement = pageElement;
        this.frame = frame;
        this.webDriverOrchestrator = webDriverOrchestrator;
    }

    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        try {
            if(frame != null) {
                webDriverOrchestrator.useFrame(this.frame);
            } else {
                webDriverOrchestrator.useDefault();
            }

            if (pageElement.canHandle(method)) {
                return method.invoke(pageElement, args);
            }

            return method.invoke(proxy, args);

        } catch (InvocationTargetException exception) {
            throw exception.getCause();
        }
    }
}
