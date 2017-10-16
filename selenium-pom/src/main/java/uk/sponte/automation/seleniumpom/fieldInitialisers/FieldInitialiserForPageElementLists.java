package uk.sponte.automation.seleniumpom.fieldInitialisers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.Annotations;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.exceptions.PageFactoryError;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.proxies.handlers.PageElementListHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by evops on 02/02/2016.
 */
public class FieldInitialiserForPageElementLists implements FieldInitialiser {
    @Inject private DependencyInjector dependencyInjector;
    @Inject private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;

    @Inject
    private Provider<PageFactory> pageFactory;

    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, FrameWrapper frame) {
        if (!FieldAssessor.isValidPageElementList(field)) return false;

        Annotations annotations = new Annotations(field);

        PageElementListHandler elementListHandler = new PageElementListHandler(dependencyInjector, searchContext, annotations.buildBy(), frame, webDriverFrameSwitchingOrchestrator);
        pageFactory.get().addListener(elementListHandler);

        List webElementListProxy = (List) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{List.class},
                elementListHandler
        );

        try {
            field.setAccessible(true);
            field.set(page, webElementListProxy);
        } catch (IllegalAccessException e) {
            throw new PageFactoryError(e.getCause());
        }

        return true;
    }
}
