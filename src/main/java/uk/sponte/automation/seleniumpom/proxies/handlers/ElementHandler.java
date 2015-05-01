package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class ElementHandler implements InvocationHandler {
    private WebDriver driver;
    private WebElement element;
    private PageElementImpl pageElement;
    private WebElement frame;

    public ElementHandler(WebDriver driver, WebElement element, PageElementImpl pageElement) {
        this(driver, element, pageElement, null);
    }

    public ElementHandler(WebDriver driver, WebElement element, PageElementImpl pageElement, WebElement frame) {
        this.driver = driver;
        this.element = element;
        this.pageElement = pageElement;
        this.frame = frame;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if(frame != null) {
                driver.switchTo().frame(frame);
            }

            if (pageElement.canHandle(method)) {
                return method.invoke(pageElement, args);
            }

            return method.invoke(this.element, args);
        } catch(InvocationTargetException exception) {
            throw exception.getCause();
        } finally {
            driver.switchTo().defaultContent();
        }
    }
}
