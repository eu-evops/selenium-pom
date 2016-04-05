package uk.sponte.automation.seleniumpom;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import uk.sponte.automation.seleniumpom.annotations.Frame;
import uk.sponte.automation.seleniumpom.configuration.Constants;
import uk.sponte.automation.seleniumpom.dependencies.DependencyFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.GuiceDependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.InternalGuiceDependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.WebDriverFactory;
import uk.sponte.automation.seleniumpom.exceptions.PageFactoryError;
import uk.sponte.automation.seleniumpom.fieldInitialisers.FieldAssessor;
import uk.sponte.automation.seleniumpom.fieldInitialisers.FieldInitialiser;
import uk.sponte.automation.seleniumpom.helpers.ClassHelper;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.helpers.ImplementationFinder;
import uk.sponte.automation.seleniumpom.helpers.ReflectionHelper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.proxies.handlers.Refreshable;
import uk.sponte.automation.seleniumpom.stolen.Annotations;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Selenium POM page factory - responsible for initialising pages with proxies
 * Created by swozniak-ba on 02/04/15.
 */
@Singleton
public class PageFactory implements
        WebDriverEventListener,
        DependencyFactory<PageFactory> {

    private final static Logger LOG = Logger
            .getLogger(PageFactory.class.getName());

    // TODO Removing public access
    @Inject
    public DependencyInjector dependencyInjector;

    @Inject
    private Set<FieldInitialiser> fieldInitialisers;

    private EventFiringWebDriver eventFiringWebDriver;

    @Inject
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;

    /*
    Default constructor using built in Guice dependency injection
     */
    public PageFactory() {
        this(new WebDriverFactory());
    }

    /*
    If users don't want to use their own DI then they can register factories
     */
    public PageFactory(DependencyFactory... factories) {
        this(null, factories);
    }

    /*
    Custom Di - recommended approach
     */
    public PageFactory(final DependencyInjector dependencyInjector,
            DependencyFactory... factories) {
        LOG.fine("Creating PageFactory");

        DependencyInjector externalDependencyInjector = dependencyInjector;

        // Is user didn't provide their own DI, we will use default one
        if (externalDependencyInjector == null) {
            externalDependencyInjector = new GuiceDependencyInjector(this,
                    factories);
        }

        GuiceDependencyInjector injector = new InternalGuiceDependencyInjector(
                this, externalDependencyInjector);

        injector.injectMembers(this);
    }

    public WebDriver getDriver() {
        eventFiringWebDriver = new EventFiringWebDriver(
                dependencyInjector.get(WebDriver.class));
        eventFiringWebDriver.register(this);

        return eventFiringWebDriver;
    }

    public <T> T get(Class<T> pageClass) throws PageFactoryError {
        return this.get(pageClass, getDriver());
    }

    public <T> T get(T page) {
        return initializeContainer(page, getDriver());
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext)
            throws PageFactoryError {
        return get(pageClass, searchContext,
                getFrame(pageClass, pageClass.getName(), null));
    }

    public <T> T get(Class<T> pageClass, SearchContext searchContext,
            FrameWrapper frame) throws PageFactoryError {
        T page = findImplementationBasedOnPageFilter(pageClass);
        return initializeContainer(page, searchContext, frame);
    }

    /* Private stuff */
    private <T> T findImplementationBasedOnPageFilter(Class<T> pageClass) {
        ImplementationFinder<T> implementationFinder = new ImplementationFinder<T>(
                pageClass, dependencyInjector);
        return implementationFinder.find();
    }

    protected <T> T initializeContainer(T page, SearchContext searchContext) {
        return this.initializeContainer(page, searchContext,
                getFrame(page.getClass(), page.getClass().getName(), null));
    }

    private FrameWrapper getFrame(AnnotatedElement element, String name,
            FrameWrapper parentFrame) {
        Frame annotation = element.getAnnotation(Frame.class);

        if (annotation == null)
            return parentFrame;

        Annotations annotations = new Annotations(element, name);
        By frameIdentifier = annotations.buildBy();

        FrameWrapper frameWrapper = new FrameWrapper(getDriver(),
                frameIdentifier);
        frameWrapper.setParent(parentFrame);

        return frameWrapper;
    }

    public <T> T initializeContainer(T page, SearchContext searchContext,
            FrameWrapper frameWrapper) {

        setRootElement(page, searchContext);

        for (Field field : ClassHelper.getFieldsFromClass(page.getClass())) {
            if (field.getName().equals(Constants.ROOT_ELEMENT_FIELD_NAME))
                continue;

            frameWrapper = getFrame(field, field.getName(), frameWrapper);

            for (FieldInitialiser fieldInitialiser : fieldInitialisers) {
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
        if (!(searchContext instanceof PageElement))
            return;

        try {
            Field rootElement = findField(pageObject.getClass(),
                    Constants.ROOT_ELEMENT_FIELD_NAME);
            rootElement.setAccessible(true);
            rootElement.set(pageObject, searchContext);
        } catch (NoSuchFieldException e) {
            // e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new PageFactoryError(e.getCause());
        }
    }

    private Field findField(Class<?> klass, String name)
            throws NoSuchFieldException {
        while (klass != null) {
            for (Field field : klass.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(name))
                    return field;
            }

            klass = klass.getSuperclass();
        }

        throw new NoSuchFieldException(
                "Could not find field with name " + name);
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
    public void beforeNavigateRefresh(WebDriver webDriver) {

    }

    @Override
    public void afterNavigateRefresh(WebDriver webDriver) {

    }

    @Override
    public void beforeFindBy(By by, WebElement webElement,
            WebDriver webDriver) {

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
    public void beforeChangeValueOf(WebElement webElement,
            WebDriver webDriver) {

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

    @Provides
    public PageFactory get() {
        LOG.fine("Got request for PageFactory");
        return this;
    }

    /**
     * Traverses the structure of the object and marks all element handlers as
     * requiring refresh - this prevents StaleElementExceptions being thrown
     * @param object Test page, section or a list of either
     */
    public void invalidate(Object object) {
        if(object instanceof Refreshable) {
            ((Refreshable) object).invalidate();
        }

        if(object instanceof Proxy) {
            InvocationHandler invocationHandler = Proxy
                    .getInvocationHandler(object);

            if(invocationHandler instanceof Refreshable) {
                ((Refreshable) invocationHandler).invalidate();
            }
        } else {
            List<Field> fields = ReflectionHelper.getAllFields(object);
            for (Field field : fields) {
                if (!FieldAssessor.isSeleniumPomField(field))
                    continue;

                Object fieldValue = ReflectionHelper
                        .getFieldValue(object, field.getName());

                invalidate(fieldValue);
            }
        }
    }
}
