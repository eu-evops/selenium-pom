package uk.sponte.automation.seleniumpom;

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
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.proxies.handlers.WebElementHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by evops on 02/02/2016.
 * Initialises fields with page sections
 */
public class FieldInitialiserForPageSections implements FieldInitialiser {
    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, WebDriver driver, PageFactory pageFactory, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        if(!isValidPageSection(field)) return false;

        Annotations annotations = new Annotations(field);

        SearchContext container = getPageElementProxy(
                driver,
                annotations.buildBy(),
                searchContext,
                field,
                frame,
                webDriverOrchestrator);

        try {
            Object pageSection = pageFactory.dependencyInjector.get(field.getType());
            pageFactory.initializeContainer(pageSection, container, frame);

            field.setAccessible(true);
            field.set(page, pageSection);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }


    @SuppressWarnings("RedundantIfStatement")
    private boolean isValidPageSection(Field field) {
        Class<?> fieldType = field.getType();

        if(List.class.isAssignableFrom(fieldType)) return false;
        if(PageElement.class.isAssignableFrom(fieldType)) return false;
        if(WebElement.class.isAssignableFrom(fieldType)) return false;

        if(field.getAnnotation(Section.class) != null) return true;
        if(PageSection.class.isAssignableFrom(fieldType)) return true;

        if(hasSeleniumFindByAnnotation(field)) return true;

        return false;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasSeleniumFindByAnnotation(Field field) {
        if(field.getAnnotation(FindBy.class) != null) return true;
        if(field.getAnnotation(FindBys.class) != null) return true;
        if(field.getAnnotation(FindAll.class) != null) return true;

        return false;
    }

    private PageElement getPageElementProxy(WebDriver driver, By by, SearchContext searchContext, Field field, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        if(frame != null && frame.frameBy.equals(by)) {
            by = By.xpath("//*");
        }

        WebElementHandler elementHandler = new WebElementHandler(driver, searchContext, by, frame, webDriverOrchestrator);
        WebElement proxyElement = (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{WebElement.class, Locatable.class,SearchContext.class, WrapsElement.class },
                elementHandler
        );

        return new PageElementImpl(driver, proxyElement);
//        InvocationHandler pageElementHandler = new PageElementHandler(pageElement, frame, webDriverOrchestrator);
//        return (PageElement) Proxy.newProxyInstance(
//                PageElement.class.getClassLoader(),
//                new Class[]{PageElement.class},
//                pageElementHandler);
    }

}
