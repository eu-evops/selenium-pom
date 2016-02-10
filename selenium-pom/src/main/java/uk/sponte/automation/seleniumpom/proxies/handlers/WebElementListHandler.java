package uk.sponte.automation.seleniumpom.proxies.handlers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Created by swozniak on 03/04/15.
 */
public class WebElementListHandler implements InvocationHandler {

    private final static Logger LOG = getLogger(WebElementListHandler.class.getName());
    private DependencyInjector driver;
    private SearchContext searchContext;
    private By by;
    private final FrameWrapper frame;
    private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;

    public WebElementListHandler(DependencyInjector driver, SearchContext searchContext, By by, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
        this.frame = frame;
        this.webDriverFrameSwitchingOrchestrator = webDriverFrameSwitchingOrchestrator;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOG.log(Level.FINER, "Calling {0} on list of WebElements", method.getName());
        List<WebElement> elements = searchContext.findElements(this.by);
        ArrayList<PageElement> pageElements = new ArrayList<PageElement>();

        for (WebElement webElement : elements) {
            WebElementHandler handler = new WebElementHandler(driver, searchContext, By.id("skldjfalksjdflkasdf"), frame, webDriverFrameSwitchingOrchestrator, webElement);

            PageElement pageElement = (PageElement) Proxy.newProxyInstance(PageElement.class.getClassLoader(),
                    new Class[]{PageElement.class},
                    handler);

            pageElements.add(pageElement);
        }

        try {
            return method.invoke(pageElements, args);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
