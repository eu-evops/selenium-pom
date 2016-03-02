package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
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
public class WebElementHandler implements InvocationHandler {
    private final static Logger LOG = Logger.getLogger(WebElementHandler.class.getName());

    private WebElement webElement;
    private DependencyInjector driver;
    private SearchContext searchContext;
    private By by;
    private FrameWrapper frame;
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;

    public WebElementHandler(DependencyInjector driver, SearchContext searchContext, By by, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator, WebElement webElement) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
        this.frame = frame;
        this.webDriverOrchestrator = webDriverFrameSwitchingOrchestrator;
        this.webElement = webElement;
    }

    public WebElementHandler(DependencyInjector driver, SearchContext searchContext, By by, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator) {
        this(driver, searchContext, by, frame, webDriverFrameSwitchingOrchestrator, null);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(!this.by.equals(By.xpath("//*"))) {
            LOG.log(Level.FINE, "Switching to frame {0}", this.frame);
            webDriverOrchestrator.useFrame(this.frame);
        }

        WebElement element = this.webElement;
        if(element == null) {
            element = new PageElementImpl(driver, searchContext.findElement(this.by));
        }

        try {
            return method.invoke(element, args);
        } catch(InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
