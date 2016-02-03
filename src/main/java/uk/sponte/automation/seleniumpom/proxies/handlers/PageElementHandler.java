package uk.sponte.automation.seleniumpom.proxies.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by swozniak on 03/04/15.
 */
public class PageElementHandler implements InvocationHandler {
    private WebDriver driver;
    private WebElement element;
    private PageElementImpl pageElement;
    private By frame;
    private WebDriverFrameSwitchingOrchestrator webDriverOrchestrator;

    public PageElementHandler(WebDriver driver, WebElement element, PageElementImpl pageElement, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        this(driver, element, pageElement, null, webDriverOrchestrator);
    }

    public PageElementHandler(WebDriver driver, WebElement element, PageElementImpl pageElement, By frame, WebDriverFrameSwitchingOrchestrator webDriverOrchestrator) {
        this.driver = driver;
        this.element = element;
        this.pageElement = pageElement;
        this.frame = frame;
        this.webDriverOrchestrator = webDriverOrchestrator;
    }


    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        try {
            if(frame != null) {
                webDriverOrchestrator.useFrame(this.frame);
            } else {
                webDriverOrchestrator.useDefault();
            }

            if (pageElement.canHandle(method)) {
                return method.invoke(pageElement, args);
            }

            return method.invoke(proxy, args);

        } catch (InvocationTargetException exception) {
            throw exception.getCause();
        }
    }
}
