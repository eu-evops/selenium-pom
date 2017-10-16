package uk.sponte.automation.seleniumpom.fieldInitialisers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.Annotations;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.annotations.Section;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.exceptions.PageFactoryError;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.proxies.handlers.PageSectionListHandler;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by evops on 02/02/2016.
 */
public class FieldInitialiserForPageSectionLists implements FieldInitialiser {
    @Inject private DependencyInjector dependencyInjector;
    @Inject private Provider<PageFactory> pageFactory;
    @Inject private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;


    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, FrameWrapper frame) {
        if(!FieldAssessor.isValidPageSectionList(field))
            return false;

        Type genericType = field.getGenericType();
        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        Type genericTypeArgument = genericTypeImpl.getActualTypeArguments()[0];
        Annotations annotations = new Annotations(field);

        PageSectionListHandler pageSectionListHandler = new PageSectionListHandler(
                dependencyInjector,
                searchContext,
                annotations.buildBy(),
                genericTypeArgument, pageFactory, frame, webDriverFrameSwitchingOrchestrator);
        pageFactory.get().addListener(pageSectionListHandler);

        Object proxyInstance = Proxy.newProxyInstance(
                Section.class.getClassLoader(),
                new Class[]{List.class},
                pageSectionListHandler
        );

        field.setAccessible(true);
        try {
            field.set(page, proxyInstance);
        } catch (IllegalAccessException e) {
            throw new PageFactoryError(e.getCause());
        }

        return true;
    }
}
