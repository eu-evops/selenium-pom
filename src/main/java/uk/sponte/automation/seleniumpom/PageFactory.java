package uk.sponte.automation.seleniumpom;

import com.google.inject.Singleton;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.reflections.Reflections;
import uk.sponte.automation.seleniumpom.annotations.Frame;
import uk.sponte.automation.seleniumpom.dependencies.DefaultDependencyInjectorImpl;
import uk.sponte.automation.seleniumpom.dependencies.DependencyFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.exceptions.PageFactoryError;
import uk.sponte.automation.seleniumpom.helpers.ClassHelper;
import uk.sponte.automation.seleniumpom.helpers.ImplementationFinder;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.stolen.Annotations;

import java.lang.reflect.*;
import java.util.*;

/**
 * Selenium POM page factory - responsible for initialising pages with proxies
 * Created by swozniak-ba on 02/04/15.
 */
@Singleton
public class PageFactory implements WebDriverEventListener {
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;
    protected DependencyInjector dependencyInjector;

    private ArrayList<FieldInitialiser> fieldInitialisers = new ArrayList<FieldInitialiser>();

    public PageFactory(
            DependencyInjector dependencyInjector,
            DependencyFactory... dependencyFactories) {

        this.dependencyInjector = dependencyInjector;

        for (DependencyFactory dependencyFactory : dependencyFactories) {
            ((DefaultDependencyInjectorImpl) this.dependencyInjector).registerFactory(dependencyFactory);
        }

        Reflections reflections = new Reflections(this.getClass().getPackage().getName());
        for (Class<? extends FieldInitialiser> aClass : reflections.getSubTypesOf(FieldInitialiser.class)) {
            try {
                FieldInitialiser fieldInitialiser = aClass.newInstance();
                fieldInitialisers.add(fieldInitialiser);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public PageFactory(DependencyFactory... dependencyFactories) {
        this(new DefaultDependencyInjectorImpl(), dependencyFactories);
    }

    public WebDriver getDriver() {
        EventFiringWebDriver webDriver = new EventFiringWebDriver(dependencyInjector.get(WebDriver.class));
        webDriver.register(this);

        return webDriver;
    }

    public <T> T get(Class<T> pageClass) throws PageFactoryError {
        return this.get(pageClass, getDriver());
    }

    public <T> T get(T page) {
        return initializeContainer(page, getDriver());
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext) throws PageFactoryError {
        return get(pageClass, searchContext, getFrame(pageClass, pageClass.getName()));
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext, By frame) throws PageFactoryError {
        T page = findImplementationBasedOnPageFilter(pageClass);
        return initializeContainer(page, searchContext, frame);
    }

    /* Private stuff */
    private <T> T findImplementationBasedOnPageFilter(Class<T> pageClass) {
        ImplementationFinder<T> implementationFinder = new ImplementationFinder<T>(pageClass, dependencyInjector);
        return implementationFinder.find();
    }

    protected <T> T initializeContainer(T page, SearchContext searchContext) {
        return this.initializeContainer(page, searchContext, getFrame(page.getClass(), page.getClass().getName()));
    }

    private By getFrame(AnnotatedElement element, String name) {
        By frame = null;
        Frame annotation = element.getAnnotation(Frame.class);
        if (annotation != null) {
            Annotations annotations = new Annotations(element, name);
            frame = annotations.buildBy();
        }

        return frame;
    }

    protected <T> T initializeContainer(T page, SearchContext searchContext, By frame) {
        useWebDriverOrchestrator();
        setRootElement(page, searchContext);
        for (Field field : ClassHelper.getFieldsFromClass(page.getClass())) {
            if (field.getName().equals("rootElement")) continue;

            By fieldFrame = getFrame(field, field.getName());
            if (fieldFrame != null) frame = fieldFrame;

            for (FieldInitialiser fieldInitialiser : fieldInitialisers) {
                if (fieldInitialiser.initialiseField(field, page, searchContext, getDriver(), this, frame, this.webDriverOrchestrator)) {
                    break;
                }
            }
        }

        return page;
    }

    private void useWebDriverOrchestrator() {
        if(webDriverOrchestrator != null) return;
        this.webDriverOrchestrator = new WebDriverFrameSwitchingOrchestrator(getDriver());
    }


    private <T> void setRootElement(T pageObject, SearchContext searchContext) {
        if (!(searchContext instanceof PageElement)) return;

        try {
            Field rootElement = findField(pageObject.getClass(), "rootElement");
            rootElement.setAccessible(true);
            rootElement.set(pageObject, searchContext);
        } catch (NoSuchFieldException e) {
            // e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new PageFactoryError(e.getCause());
        }
    }

    private Field findField(Class<?> klass, String name) throws NoSuchFieldException {
        while (klass != null) {
            for (Field field : klass.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(name))
                    return field;
            }

            klass = klass.getSuperclass();
        }

        throw new NoSuchFieldException("Could not find field with name " + name);
    }

    @Override
    public void beforeNavigateTo(String s, WebDriver webDriver) {

    }

    @Override
    public void afterNavigateTo(String s, WebDriver webDriver) {
        useWebDriverOrchestrator();
        this.webDriverOrchestrator.useDefault();
    }

    @Override
    public void beforeNavigateBack(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateBack(WebDriver webDriver) {

    }

    @Override
    public void beforeNavigateForward(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateForward(WebDriver webDriver) {

    }

    @Override
    public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void beforeClickOn(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void afterClickOn(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver) {

    }

    @Override
    public void beforeScript(String s, WebDriver webDriver) {

    }

    @Override
    public void afterScript(String s, WebDriver webDriver) {

    }

    @Override
    public void onException(Throwable throwable, WebDriver webDriver) {

    }
}
