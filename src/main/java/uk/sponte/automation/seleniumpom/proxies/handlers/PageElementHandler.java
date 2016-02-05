package uk.sponte.automation.seleniumpom.proxies.handlers;

import uk.sponte.automation.seleniumpom.PageElementImpl;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by swozniak on 03/04/15.
 */
public class PageElementHandler implements InvocationHandler {
    private static final Logger LOG = Logger.getLogger(PageElementHandler.class.getName());

    private PageElementImpl pageElement;
    private FrameWrapper frame;
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;

    public PageElementHandler(PageElementImpl pageElement, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        this.pageElement = pageElement;
        this.frame = frame;
        this.webDriverOrchestrator = webDriverOrchestrator;
    }

    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        LOG.log(Level.FINER, "Invoking {0}", method.getName());
        try {
            if(frame != null) {
                LOG.log(Level.FINER, "Frame associated with element");
                webDriverOrchestrator.useFrame(this.frame);
            } else {
                LOG.log(Level.FINER, "No frame associated with element");
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
