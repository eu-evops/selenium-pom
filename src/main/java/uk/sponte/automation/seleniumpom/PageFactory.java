package uk.sponte.automation.seleniumpom;

import com.google.inject.Singleton;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;
import uk.sponte.automation.seleniumpom.annotations.Section;
import uk.sponte.automation.seleniumpom.dependencies.DefaultDependencyInjectorImpl;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.exceptions.PageFactoryError;
import uk.sponte.automation.seleniumpom.proxies.handlers.*;

import java.lang.reflect.*;
import java.util.List;

/**
 * Selenium POM page factory - responsible for initialising pages with proxies
 * Created by swozniak-ba on 02/04/15.
 */
@Singleton
public class PageFactory {
    private DependencyInjector dependencyInjector;

    public PageFactory() {
        this(new DefaultDependencyInjectorImpl());
    }

    public WebDriver getDriver() {
        return dependencyInjector.get(WebDriver.class);
    }

    public PageFactory(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
    }

    public <T> T get(Class<T> pageClass) throws PageFactoryError {
        return this.get(pageClass, dependencyInjector.get(WebDriver.class));
    }

    public <T> T get(T page) {
        return initializeContainer(page, dependencyInjector.get(WebDriver.class));
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext) throws PageFactoryError {
        T page = dependencyInjector.get(pageClass);
        return initializeContainer(page, searchContext);
    }

    private <T> T initializeContainer(T page, SearchContext searchContext) {
        setRootElement(page, searchContext);
        for (Field field : page.getClass().getFields()) {
            initializePageElements(field, page, searchContext);
            initializePageElementLists(field, page, searchContext);
            initializePageSections(field, page, searchContext);
            initializePageSectionLists(field, page, searchContext);
        }

        return page;
    }

    private <T> void setRootElement(T pageObject, SearchContext searchContext) {
        if(!(searchContext instanceof PageElement)) return;

        try {
            Field rootElement = findField(pageObject, "rootElement");
            rootElement.setAccessible(true);
            rootElement.set(pageObject, searchContext);
        } catch (NoSuchFieldException e) {
            // e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new PageFactoryError(e.getCause());
        }
    }

    public Field findField(Object object, String name) throws NoSuchFieldException {
        Class klass = object.getClass();

        while(klass != null) {
            for (Field field : klass.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(name))
                    return field;
            }

            klass = klass.getSuperclass();
        }

        throw new NoSuchFieldException("Could not find field with name " + name);
    }

    private <T> void initializePageSectionLists(Field field, T page, SearchContext searchContext) {
        if (!List.class.isAssignableFrom(field.getType())) return;
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return;
        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        if (PageElement.class.isAssignableFrom((Class<?>) genericTypeImpl.getActualTypeArguments()[0])) return;
        if (field.getAnnotation(Section.class) == null) return;

        Type genericTypeArgument = genericTypeImpl.getActualTypeArguments()[0];
        Annotations annotations = new Annotations(field);

        PageSectionListHandler pageSectionListHandler = new PageSectionListHandler(
                getDriver(),
                searchContext,
                annotations.buildBy(),
                genericTypeArgument,
                this);

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
    }

    private <T> void initializePageSections(Field field, T page, SearchContext searchContext) {
        if (List.class.isAssignableFrom(field.getType())) return;
        if (PageElement.class.isAssignableFrom(field.getType())) return;

        if (PageSection.class.isAssignableFrom(field.getType()) &&
                field.getAnnotation(Section.class) == null &&
                field.getAnnotation(FindBy.class) == null &&
                field.getAnnotation(FindBys.class) == null &&
                field.getAnnotation(FindAll.class) == null
                ) return;

        WebDriver webDriver = dependencyInjector.get(WebDriver.class);
        Annotations annotations = new Annotations(field);

        SearchContext container = getPageElementProxy(
                webDriver,
                annotations.buildBy(),
                searchContext,
                field);

        try {
            Object pageSection = dependencyInjector.get(field.getType());
            this.initializeContainer(pageSection, container);

            field.setAccessible(true);
            field.set(page, pageSection);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T> void initializePageElementLists(Field field, T page, SearchContext searchContext) {
        Class<?> fieldType = field.getType();
        if (!List.class.isAssignableFrom(fieldType)) return;
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return;
        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        if (!PageElement.class.isAssignableFrom((Class<?>) genericTypeImpl.getActualTypeArguments()[0])) return;

        Annotations annotations = new Annotations(field);
        WebElementListHandler elementListHandler = new WebElementListHandler(searchContext, annotations.buildBy());

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
    }

    private <T> void initializePageElements(Field field, T page, SearchContext searchContext) {
        if (PageElement.class.isAssignableFrom(field.getType())) {

            WebDriver webDriver = dependencyInjector.get(WebDriver.class);
            Annotations annotations = new Annotations(field);

            try {
                PageElement pageElementProxy = getPageElementProxy(webDriver, annotations.buildBy(), searchContext, field);

                field.setAccessible(true);
                field.set(page, pageElementProxy);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private PageElement getPageElementProxy(WebDriver driver, By by, SearchContext searchContext, Field field) {
        WebElementHandler elementHandler = new WebElementHandler(searchContext, by);
        WebElement proxyElement = (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{WebElement.class, Locatable.class,SearchContext.class, WrapsElement.class },
                elementHandler
        );

        PageElement pageElement = new PageElementImpl(driver, proxyElement, field);
        InvocationHandler pageElementHandler = new ElementHandler(proxyElement, pageElement);
        return (PageElement) Proxy.newProxyInstance(
                PageElement.class.getClassLoader(),
                new Class[]{PageElement.class},
                pageElementHandler);
    }
}
