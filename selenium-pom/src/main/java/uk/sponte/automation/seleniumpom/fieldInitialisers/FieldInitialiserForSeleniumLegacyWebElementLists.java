package uk.sponte.automation.seleniumpom.fieldInitialisers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
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

import java.lang.reflect.*;
import java.util.List;

/**
 * Created by evops on 02/02/2016.
 */
public class FieldInitialiserForSeleniumLegacyWebElementLists implements FieldInitialiser {
    @Inject private DependencyInjector dependencyInjector;
    @Inject private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;

    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, FrameWrapper frame) {
        if (isValidWebElementField(field)) return false;

        Annotations annotations = new Annotations(field);
        WebElementListHandler elementListHandler = new WebElementListHandler(dependencyInjector, searchContext, annotations.buildBy(), frame, webDriverFrameSwitchingOrchestrator);

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

    private boolean isValidWebElementField(Field field) {
        Class<?> fieldType = field.getType();
        if (!List.class.isAssignableFrom(fieldType)) return true;
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return true;
        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        if (!WebElement.class.isAssignableFrom((Class<?>)genericTypeImpl.getActualTypeArguments()[0])) return true;
        return false;
    }
}
