package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;
import uk.sponte.automation.seleniumpom.configuration.Constants;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.helpers.ReflectionHelper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Created by swozniak on 03/04/15.
 */
public class WebElementListHandler implements InvocationHandler, Refreshable {

    private final static Logger LOG = getLogger(
            WebElementListHandler.class.getName());

    private DependencyInjector driver;

    private SearchContext searchContext;

    private By by;

    private final FrameWrapper frame;

    private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;

    private ArrayList<WebElement> webElements;

    private Refreshable parent;

    public WebElementListHandler(DependencyInjector driver,
            SearchContext searchContext, By by, FrameWrapper frame,
            WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.by = by;
        this.frame = frame;
        this.webDriverFrameSwitchingOrchestrator = webDriverFrameSwitchingOrchestrator;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (webElements == null) {
            List<WebElement> elements = searchContext.findElements(this.by);
            webElements = new ArrayList<WebElement>();

            for (WebElement webElement : elements) {
                webElements.add(getWebElementProxy(webElement));
            }
        }

        try {
            LOG.fine(String.format("Calling %s on %s", method.getName(), this));
            return method.invoke(webElements, args);
        } catch (InvocationTargetException ex) {
            LOG.fine(String.format("Error calling %s on %s", method.getName(), this));
            throw ex.getCause();
        }
    }

    private WebElement getWebElementProxy(WebElement webElement) {
        WebElementHandler handler = new WebElementHandler(driver,
                searchContext,
                By.id(Constants.DUMMY_PAGE_LOCATOR_FOR_LISTS),
                frame,
                webDriverFrameSwitchingOrchestrator,
                webElement
        );
        handler.setParent(this);

        return (WebElement) Proxy
                .newProxyInstance(
                        WebElement.class.getClassLoader(),
                        new Class[] {
                                WebElement.class,
                                Locatable.class,
                                SearchContext.class,
                                WrapsElement.class
                        },
                        handler
                );
    }

    public void invalidate() {
        if(webElements == null) return;

        for (WebElement webElement : webElements) {
            if(webElement instanceof Proxy) {
                InvocationHandler invocationHandler = Proxy
                        .getInvocationHandler(webElement);

                if(invocationHandler instanceof Refreshable) {
                    ((Refreshable) invocationHandler).refresh();
                }
            }
        }
    }

    @Override
    public void refresh() {
        if(webElements == null) return;

        List<WebElement> elements = searchContext.findElements(by);
        Field elementField = ReflectionHelper
                .getField(PageElementImpl.class,
                        Constants.PAGE_ELEMENT_CONTAINER_FIELD_NAME);

        assert elementField != null;
        elementField.setAccessible(true);

        for (int i = 0; i < elements.size(); i++) {
            WebElement e = elements.get(i);
            Object s = webElements.get(i);

            if (s == null) {
                webElements.set(i, getWebElementProxy(e));
            } else {
                try {
                    elementField.set(s, e);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setParent(Refreshable refreshable) {
        this.parent = refreshable;
    }
}
