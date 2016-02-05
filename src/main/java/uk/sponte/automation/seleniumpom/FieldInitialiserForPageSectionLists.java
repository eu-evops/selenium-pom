package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;
import uk.sponte.automation.seleniumpom.annotations.Section;
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
    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, WebDriver driver, PageFactory pageFactory, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        if(!isValidPageSectionList(field))
            return false;

        Type genericType = field.getGenericType();
        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        Type genericTypeArgument = genericTypeImpl.getActualTypeArguments()[0];
        Annotations annotations = new Annotations(field);

        PageSectionListHandler pageSectionListHandler = new PageSectionListHandler(
                driver,
                searchContext,
                annotations.buildBy(),
                genericTypeArgument, pageFactory, frame, webDriverOrchestrator);

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

    @SuppressWarnings("RedundantIfStatement")
    private boolean isValidPageSectionList(Field field) {
        // return false if it's not a list
        if (!List.class.isAssignableFrom(field.getType())) return false;

        // If we marked field with Section annotation, I'll assume you know what you're doing
        if (field.isAnnotationPresent(Section.class)) return true;

        // If it's not generic, return false
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return false;

        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        Class<?> genericTypeArgument = (Class<?>) genericTypeImpl.getActualTypeArguments()[0];

        // PageElement list is not pageSection
        if (PageElement.class.isAssignableFrom(genericTypeArgument)) return false;
        if (WebElement.class.isAssignableFrom(genericTypeArgument)) return false;
        if (PageSection.class.isAssignableFrom(genericTypeArgument)) return true;

        // If it's a list and has FindBy annotation it's a valid page section list as far as we're concerned
        if (field.isAnnotationPresent(FindBy.class)) return true;
        if (field.isAnnotationPresent(FindBys.class)) return true;
        if (field.isAnnotationPresent(FindAll.class)) return true;

        return false;
    }
}
