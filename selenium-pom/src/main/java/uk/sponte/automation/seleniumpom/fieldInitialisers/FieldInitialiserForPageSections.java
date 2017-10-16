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
 * Initialises fields with page sections
 */
public class FieldInitialiserForPageSections implements FieldInitialiser {
    @Inject private DependencyInjector dependencyInjector;
    @Inject private Provider<PageFactory> pageFactory;
    @Inject private WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator;

    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, FrameWrapper frame) {
        if(!FieldAssessor.isValidPageSection(field)) return false;

        Annotations annotations = new Annotations(field);

        SearchContext container = getPageElementProxy(
                annotations.buildBy(),
                searchContext,
                frame
        );

        try {
            Object pageSection = dependencyInjector.get(field.getType());
            pageFactory.get().initializeContainer(pageSection, container, frame);

            field.setAccessible(true);
            field.set(page, pageSection);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }


    private PageElement getPageElementProxy(By by,
            SearchContext searchContext, FrameWrapper frame) {
        if(frame != null && frame.frameBy.equals(by)) {
            by = By.xpath("//*");
        }

        WebElementHandler elementHandler = new WebElementHandler(
                this.dependencyInjector, searchContext, by, frame, webDriverFrameSwitchingOrchestrator);
        pageFactory.get().addListener(elementHandler);

        WebElement proxyElement = (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{WebElement.class, Locatable.class,SearchContext.class, WrapsElement.class },
                elementHandler
        );

        return new PageElementImpl(proxyElement);
    }

}
