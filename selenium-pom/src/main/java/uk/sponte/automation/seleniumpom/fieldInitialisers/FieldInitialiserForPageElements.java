package uk.sponte.automation.seleniumpom.fieldInitialisers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.Annotations;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.proxies.handlers.WebElementHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by evops on 02/02/2016.
 */
public class FieldInitialiserForPageElements implements FieldInitialiser {
    @Inject
    private DependencyInjector dependencyInjector;

    @Inject
    private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;

    @Inject
    private Provider<PageFactory> pageFactory;

    @Override
    public Boolean initialiseField(Field field, Object page,
            SearchContext searchContext, FrameWrapper frame) {
        if (!FieldAssessor.isValidPageElement(field))
            return false;

        Annotations annotations = new Annotations(field);

        try {
            PageElement pageElementProxy = getPageElementProxy(
                    dependencyInjector, annotations.buildBy(), searchContext,
                    frame, webDriverFrameSwitchingOrchestrator);

            field.setAccessible(true);
            field.set(page, pageElementProxy);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    private PageElement getPageElementProxy(DependencyInjector driver, By by,
            SearchContext searchContext, FrameWrapper frame,
            WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator) {
        WebElementHandler elementHandler = new WebElementHandler(driver,
                searchContext, by, frame, webDriverFrameSwitchingOrchestrator);

        WebElement proxyElement = (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[] { WebElement.class, Locatable.class,
                        SearchContext.class, WrapsElement.class },
                elementHandler
        );

        return new PageElementImpl(proxyElement);
    }

}
