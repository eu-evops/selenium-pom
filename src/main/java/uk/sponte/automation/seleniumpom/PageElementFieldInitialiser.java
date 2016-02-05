package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.Annotations;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.proxies.handlers.PageElementHandler;
import uk.sponte.automation.seleniumpom.proxies.handlers.WebElementHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by evops on 02/02/2016.
 */
public class PageElementFieldInitialiser implements FieldInitialiser {
    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, WebDriver driver, PageFactory pageFactory, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        if (PageElement.class.isAssignableFrom(field.getType())) {

            Annotations annotations = new Annotations(field);

            try {
                PageElement pageElementProxy = getPageElementProxy(driver, annotations.buildBy(), searchContext, field, frame, webDriverOrchestrator);

                field.setAccessible(true);
                field.set(page, pageElementProxy);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return true;
        } else {
            return false;
        }
    }


    private PageElement getPageElementProxy(WebDriver driver, By by, SearchContext searchContext, Field field, FrameWrapper frame, WebDriverFrameSwitchingOrchestrator webDriverFrameSwitchingOrchestrator) {
        WebElementHandler elementHandler = new WebElementHandler(driver, searchContext, by);
        WebElement proxyElement = (WebElement) Proxy.newProxyInstance(
                WebElement.class.getClassLoader(),
                new Class[]{WebElement.class, Locatable.class,SearchContext.class, WrapsElement.class },
                elementHandler
        );

        PageElementImpl pageElement = new PageElementImpl(driver, proxyElement);
        InvocationHandler pageElementHandler = new PageElementHandler(pageElement, frame, webDriverFrameSwitchingOrchestrator);
        return (PageElement) Proxy.newProxyInstance(
                PageElement.class.getClassLoader(),
                new Class[]{PageElement.class},
                pageElementHandler);
    }

}
