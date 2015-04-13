package uk.sponte.automation.web;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * Created by swozniak on 03/04/15.
 */
public class WebElementExtensionsImpl implements WebElementExtensions {
    private final static Integer DEFAULT_TIMEOUT = 5000;

    private WebDriver driver;
    private SearchContext searchContext;
    private WebElement element;
    private Field field;

    public WebElementExtensionsImpl(
            WebDriver driver,
            SearchContext searchContext,
            WebElement element,
            Field field) {
        this.driver = driver;
        this.searchContext = searchContext;
        this.element = element;
        this.field = field;
    }

    public Boolean canHandle(Method methodName) {
        for (Method method : this.getClass().getMethods()) {
            if (method.getName().equals(methodName.getName()))
                return true;
        }

        return false;
    }

    @Override
    public boolean isPresent() {
        try {
            this.element.getTagName();
        } catch(NoSuchElementException | StaleElementReferenceException ex) {
            return false;
        }

        return true;
    }

    public String getValue() {
        return this.element.getAttribute("value");
    }

    public void set(String text) {
        String tagName = this.element.getTagName();
        if(tagName.equalsIgnoreCase("input")) {
            this.element.clear();
            this.element.sendKeys(text);
            return;
        }

        if(tagName.equalsIgnoreCase("select")) {
            Select select = new Select(this.element);
            select.selectByValue(text);
            return;
        }

        throw new Error("Cannot set elements value: " + tagName);
    }

    @Override
    public void set(String format, Object... args) {
        this.set(String.format(format, args));
    }

    public void doubleClick() {
        new Actions(driver).doubleClick(this.element).perform();
    }

    public void dropOnto(PageElement target) {
        new Actions(driver).dragAndDrop(this.element, target).perform();
    }

    public void waitFor(Integer timeout) throws TimeoutException {
        long start = Calendar.getInstance().getTimeInMillis();
        while (true) {
            if (Calendar.getInstance().getTimeInMillis() - start > timeout) {
                throw new TimeoutException("Timed out while waiting for element to be present: " + this.field);
            }
            try {
                this.element.getTagName();
                return;
            } catch (NoSuchElementException | StaleElementReferenceException webDriverException) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    public void waitFor() throws TimeoutException {
        waitFor(DEFAULT_TIMEOUT);
    }

    public void waitUntilGone(Integer timeout) throws TimeoutException {
        long start = Calendar.getInstance().getTimeInMillis();
        while (true) {
            if (Calendar.getInstance().getTimeInMillis() - start > timeout) {
                throw new TimeoutException("Timed out while waiting for element to be gone: " + this.field);
            }
            try {
                this.element.getTagName();
                Thread.sleep(100);
            } catch (StaleElementReferenceException ex) {
                return;
            } catch (NoSuchElementException ex) {
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void waitUntilGone() throws TimeoutException {
        waitUntilGone(DEFAULT_TIMEOUT);
    }

    public void waitUntilHidden(Integer timeout) throws TimeoutException {
        long start = Calendar.getInstance().getTimeInMillis();
        while (this.element.isDisplayed()) {
            if (Calendar.getInstance().getTimeInMillis() - start > timeout) {
                throw new TimeoutException("Timed out while waiting for element to be hidden: " + this.field);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void waitUntilHidden() throws TimeoutException {
        waitUntilHidden(DEFAULT_TIMEOUT);
    }

    public void waitUntilVisible(Integer timeout) throws TimeoutException {
        long start = Calendar.getInstance().getTimeInMillis();

        waitFor(timeout);

        while (!this.element.isDisplayed()) {
            if (Calendar.getInstance().getTimeInMillis() - start > timeout) {
                throw new TimeoutException("Timed out while waiting for element to be visible: " + this.field);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void waitUntilVisible() throws TimeoutException {
        waitUntilVisible(DEFAULT_TIMEOUT);
    }
}
