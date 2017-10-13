package uk.sponte.automation.seleniumpom.fieldInitialisers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.Annotations;
import uk.sponte.automation.seleniumpom.ElementListImpl;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.exceptions.PageFactoryError;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.proxies.handlers.ElementListHandler;
import uk.sponte.automation.seleniumpom.proxies.handlers.WebElementListHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by evops on 02/02/2016.
 */
public class FieldInitialiserForSeleniumLegacyWebElementLists implements FieldInitialiser {
    @Inject private DependencyInjector dependencyInjector;
    @Inject private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;
    @Inject private Provider<PageFactory> pageFactory;

    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, FrameWrapper frame) {
        if (!FieldAssessor.isValidWebElementList(field)) return false;

        Annotations annotations = new Annotations(field);
        WebElementListHandler elementListHandler = new WebElementListHandler(dependencyInjector, searchContext, annotations.buildBy(), frame, webDriverFrameSwitchingOrchestrator);
        pageFactory.get().addListener(elementListHandler);

        List webElementListProxy = (List) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{List.class},
                elementListHandler
        );

        ElementListImpl webElementListExtensions = new ElementListImpl(searchContext, webElementListProxy);
        InvocationHandler pageElementHandler = new ElementListHandler(webElementListProxy, webElementListExtensions);

        List pageElementListProxy = (List) Proxy.newProxyInstance(
                PageElement.class.getClassLoader(),
                new Class[]{List.class},
                pageElementHandler);

        try {
            field.setAccessible(true);
            field.set(page, pageElementListProxy);
        } catch (IllegalAccessException e) {
            throw new PageFactoryError(e.getCause());
        }

        return true;
    }
}
