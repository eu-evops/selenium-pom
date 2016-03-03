package uk.sponte.automation.seleniumpom;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import uk.sponte.automation.seleniumpom.annotations.Frame;
import uk.sponte.automation.seleniumpom.dependencies.DependencyFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.GuiceDependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.WebDriverFactory;
import uk.sponte.automation.seleniumpom.exceptions.PageFactoryError;
import uk.sponte.automation.seleniumpom.fieldInitialisers.FieldInitialiser;
import uk.sponte.automation.seleniumpom.helpers.*;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.stolen.Annotations;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Selenium POM page factory - responsible for initialising pages with proxies
 * Created by swozniak-ba on 02/04/15.
 */
@Singleton
public class PageFactory implements WebDriverEventListener, DependencyFactory<PageFactory> {
    private final static Logger LOG = Logger.getLogger(PageFactory.class.getName());

    // TODO Removing public access
    @Inject
    public DependencyInjector dependencyInjector;

    @Inject
    private Set<FieldInitialiser> fieldInitialisers;
    private EventFiringWebDriver eventFiringWebDriver;

    @Inject
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;
    private GuiceDependencyInjector internalDependencyInjector;

    /*
    Default constructor using built in guice dependency injection
     */
    public PageFactory() {
        this(new GuiceDependencyInjector(new WebDriverFactory()));
    }

    /*
    If users don't want to use their own DI then they can register factories
     */
    public PageFactory(DependencyFactory... factories) {
        this(new GuiceDependencyInjector(new GuiceDependencyInjector(), factories));
    }

    /*
    Custom Di - recommended approach
     */
    public PageFactory(final DependencyInjector dependencyInjector) {
        final PageFactory self = this;

        LOG.info("Creating PageFactory");
        this.internalDependencyInjector = new GuiceDependencyInjector(dependencyInjector, new DependencyFactory<PageFactory>() {
            @Override
            public PageFactory get() {
                return self;
            }
        });

        internalDependencyInjector.injectMembers(this);
    }

    public WebDriver getDriver() {
        LOG.log(Level.FINE, "Requested a driver instance");
        if(eventFiringWebDriver != null)
            return eventFiringWebDriver;

        LOG.log(Level.FINE, "Webdriver was not initialised, creating new");
        // TODO need to look into this - this is not right lol
        eventFiringWebDriver = new EventFiringWebDriver(dependencyInjector.get(WebDriver.class));
        eventFiringWebDriver.register(this);

        return eventFiringWebDriver;
    }

    public <T> T get(Class<T> pageClass) throws PageFactoryError {
        return this.get(pageClass, getDriver());
    }

    public <T> T get(T page) {
        return initializeContainer(page, getDriver());
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext) throws PageFactoryError {
        return get(pageClass, searchContext, getFrame(pageClass, pageClass.getName(), null));
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext, FrameWrapper frame) throws PageFactoryError {
        T page = findImplementationBasedOnPageFilter(pageClass);
        return initializeContainer(page, searchContext, frame);
    }

    /* Private stuff */
    private <T> T findImplementationBasedOnPageFilter(Class<T> pageClass) {
        ImplementationFinder<T> implementationFinder = new ImplementationFinder<T>(pageClass, dependencyInjector);
        return implementationFinder.find();
    }

    protected <T> T initializeContainer(T page, SearchContext searchContext) {
        return this.initializeContainer(page, searchContext, getFrame(page.getClass(), page.getClass().getName(), null));
    }

    private FrameWrapper getFrame(AnnotatedElement element, String name, FrameWrapper parentFrame) {
        Frame annotation = element.getAnnotation(Frame.class);

        if(annotation == null) return parentFrame;

        Annotations annotations = new Annotations(element, name);
        By frameIdentifier = annotations.buildBy();

        FrameWrapper frameWrapper = new FrameWrapper(getDriver(), frameIdentifier);
        frameWrapper.setParent(parentFrame);

        return frameWrapper;
    }

    public <T> T initializeContainer(T page, SearchContext searchContext, FrameWrapper frameWrapper) {
        setRootElement(page, searchContext);
        for (Field field : ClassHelper.getFieldsFromClass(page.getClass())) {
            if (field.getName().equals("rootElement")) continue;

            frameWrapper = getFrame(field, field.getName(), frameWrapper);

            for (FieldInitialiser fieldInitialiser : fieldInitialisers) {
                internalDependencyInjector.injectMembers(fieldInitialiser);
                if (fieldInitialiser.initialiseField(
                        field,
                        page,
                        searchContext,
                        frameWrapper)) {
                    break;
                }
            }
        }

        return page;
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
        this.webDriverOrchestrator.useFrame(null);
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

    @Override
    public PageFactory get() {
        return this;
    }
}
