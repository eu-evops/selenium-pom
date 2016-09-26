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
public class PageElementListHandler implements InvocationHandler, Refreshable {

    private final static Logger LOG = getLogger(
            PageElementListHandler.class.getName());

    private DependencyInjector driver;

    private SearchContext searchContext;

    private By by;

    private final FrameWrapper frame;

    private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;

    private ArrayList<WebElement> webElements;

    private Refreshable parent;

    private boolean needsRefresh;

    public PageElementListHandler(DependencyInjector driver,
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
            List<WebElement> elements = this.searchContext.findElements(this.by);
            webElements = new ArrayList<WebElement>();

            for (WebElement webElement : elements) {
                webElements.add(getPageElementProxy(webElement));
            }
        } else if (this.needsRefresh) {
            refresh();
            this.needsRefresh = false;
        }

        try {
            LOG.fine(String.format("Calling %s on %s", method.getName(), this));
            return method.invoke(webElements, args);
        } catch (InvocationTargetException ex) {
            LOG.fine(String.format("Error calling %s on %s", method.getName(), this));
            throw ex.getCause();
        }
    }

    private WebElement getPageElementProxy(WebElement webElement) {
        WebElementHandler handler = new WebElementHandler(driver,
                searchContext,
                By.id(Constants.DUMMY_PAGE_LOCATOR_FOR_LISTS),
                frame,
                webDriverFrameSwitchingOrchestrator,
                webElement
        );
        handler.setParent(this);

        WebElement webElementProxy = (WebElement) Proxy
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

        return new PageElementImpl(webElementProxy);
    }

    @Override
    public void invalidate() {
        this.needsRefresh = true;
        if(webElements == null) return;
        for (WebElement webElement : this.webElements) {
            if(webElement instanceof PageElementImpl) {
                WebElement wrappedElement = ((PageElementImpl) webElement)
                        .getWrappedElement();
                if(wrappedElement instanceof Proxy) {
                    InvocationHandler invocationHandler = Proxy
                            .getInvocationHandler(wrappedElement);
                    if(invocationHandler instanceof Refreshable) {
                        ((Refreshable) invocationHandler).invalidate();
                    }
                }
            }
        }
    }

    public void refresh() {
        if(webElements == null) return;

        List<WebElement> elements = this.searchContext.findElements(by);
        Field elementField = ReflectionHelper
                .getField(PageElementImpl.class,
                        Constants.PAGE_ELEMENT_CONTAINER_FIELD_NAME);

        Field webElementInvocationHandlerWebElementField = ReflectionHelper
                .getField(WebElementHandler.class,
                        Constants.PAGE_ELEMENT_CONTAINER_FIELD_NAME);
        webElementInvocationHandlerWebElementField.setAccessible(true);


        assert elementField != null;
        elementField.setAccessible(true);

        for (int i = 0; i < elements.size(); i++) {
            WebElement e = elements.get(i);

            if (webElements.size() == i) {
                webElements.add(getPageElementProxy(e));
            } else {
                Object s = webElements.get(i);
                try {
                    Object webElementProxy = elementField.get(s);
                    if(webElementProxy instanceof Proxy) {
                        InvocationHandler invocationHandler = Proxy
                                .getInvocationHandler(webElementProxy);
                        if(invocationHandler instanceof WebElementHandler) {
                            webElementInvocationHandlerWebElementField.set(invocationHandler, e);
                        }
                    }
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
