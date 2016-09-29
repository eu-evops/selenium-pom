package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Created by swozniak on 03/04/15.
 */
public class WebElementHandler implements InvocationHandler, Refreshable {
    private final static Logger LOG = Logger.getLogger(WebElementHandler.class.getName());

    private WebElement webElement;
    private DependencyInjector dependencyInjector;
    private SearchContext searchContext;
    private By by;
    private FrameWrapper frame;
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;

    boolean needsRefresh = false;

    private Refreshable parent;

    public WebElementHandler(DependencyInjector dependencyInjector, SearchContext searchContext, By by, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator) {
        this(dependencyInjector, searchContext, by, frame, webDriverFrameSwitchingOrchestrator, null);
    }

    public WebElementHandler(DependencyInjector dependencyInjector, SearchContext searchContext, By by, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator, WebElement webElement) {
        this.dependencyInjector = dependencyInjector;
        this.searchContext = searchContext;
        this.by = by;
        this.frame = frame;
        this.webDriverOrchestrator = webDriverFrameSwitchingOrchestrator;
        this.webElement = webElement;
    }

    public WebDriver getDriver(){
        return this.dependencyInjector.get(WebDriver.class);
    }

    public void invalidate() {
        needsRefresh = true;
    }

    public void refresh() {
        this.webElement = null;
    }

    @Override
    public void setParent(Refreshable refreshable) {
        this.parent = refreshable;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO This needs refactoring - xpath //* assumes it's an xpath for iframe document
        if(!this.by.equals(By.xpath("//*"))) {
            webDriverOrchestrator.useFrame(this.frame);
        }

        // only refresh if it's not automatically assigned through list handler
        if(needsRefresh) {
            if(parent == null) {
                this.webElement = searchContext.findElement(this.by);
            } else {
                this.webElement = null;
                this.parent.refresh();
            }
            needsRefresh = false;
        }

        if(this.webElement == null) {
            this.webElement = searchContext.findElement(this.by);
        }

        try {
            return method.invoke(this.webElement, args);
        } catch(InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
