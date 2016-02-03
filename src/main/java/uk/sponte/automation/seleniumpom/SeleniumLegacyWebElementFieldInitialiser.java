package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.Annotations;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;
import uk.sponte.automation.seleniumpom.proxies.handlers.WebElementHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by evops on 02/02/2016.
 */
public class SeleniumLegacyWebElementFieldInitialiser implements FieldInitialiser {
    @Override
    public Boolean initialiseField(Field field, Object page, SearchContext searchContext, WebDriver driver, PageFactory pageFactory, By frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        {
            if (!PageElement.class.isAssignableFrom(field.getType()) &&
                    WebElement.class.isAssignableFrom(field.getType())) {

                Annotations annotations = new Annotations(field);

                try {
                    WebElementHandler elementHandler = new WebElementHandler(driver, searchContext, annotations.buildBy());
                    WebElement proxyElement = (WebElement) Proxy.newProxyInstance(
                            WebElement.class.getClassLoader(),
                            new Class[]{WebElement.class, Locatable.class,SearchContext.class, WrapsElement.class },
                            elementHandler
                    );

                    field.setAccessible(true);
                    field.set(page, proxyElement);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
