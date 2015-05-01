package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Select;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Thin wrapper around selenium WebElement. It also adds
 * extension methods to simplify interacting with elements
 * in HTML.
 * Created by swozniak on 03/04/15.
 */
public class PageElementImpl implements PageElement {
    private final static Integer DEFAULT_TIMEOUT = 5000;

    // packagePrivate
    WebElement frame;

    private WebDriver driver;
    private WebElement element;
    private String windowHandle;

    public PageElementImpl(
            WebDriver driver,
            WebElement element) {
        this.driver = driver;
        this.element = element;
    }

    public boolean canHandle(Method methodName) {
        for (Method method : this.getClass().getMethods()) {
            if (method.getName().equals(methodName.getName()))
                return true;
        }

        return false;
    }

    public boolean isPresent() {
        try {
            this.element.getTagName();
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException ex) {
            return false;
        }

        return true;
    }

    public String getHiddenText() {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].innerText;", element);
    }

    public String getValue() {
        return this.element.getAttribute("value");
    }

    public void set(String text) {
        String tagName = this.element.getTagName();
        if (tagName.equalsIgnoreCase("input")) {
            this.element.clear();
            this.element.sendKeys(text);
            return;
        }

        if (tagName.equalsIgnoreCase("select")) {
            Select select = new Select(this.element);
            select.selectByValue(text);
            return;
        }

        throw new Error("Cannot set elements value: " + tagName);
    }

    public void set(String format, Object... args) {
        this.set(String.format(format, args));
    }


    // DEMO custom actions made easier
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
                throw new TimeoutException("Timed out while waiting for element to be present");
            }
            try {
                this.element.getTagName();
                return;
            } catch (NoSuchElementException webDriverException) {
                sleep(100);
            } catch (StaleElementReferenceException webDriverException) {
                sleep(100);
            }
        }
    }

    private void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitFor() throws TimeoutException {
        waitFor(DEFAULT_TIMEOUT);
    }

    public void waitUntilGone(Integer timeout) throws TimeoutException {
        long start = Calendar.getInstance().getTimeInMillis();
        while (true) {
            if (Calendar.getInstance().getTimeInMillis() - start > timeout) {
                throw new TimeoutException("Timed out while waiting for element to be gone");
            }
            try {
                this.element.getTagName();
                sleep(100);
            } catch (StaleElementReferenceException ex) {
                return;
            } catch (NoSuchElementException ex) {
                return;
            }
        }
    }

    public void waitUntilGone() throws TimeoutException {
        waitUntilGone(DEFAULT_TIMEOUT);
    }

    // DEMO waiting for things to happen, for instance waiting for element to disappear from the page
    public void waitUntilHidden(Integer timeout) throws TimeoutException {
        long start = Calendar.getInstance().getTimeInMillis();
        while (this.element.isDisplayed()) {
            if (Calendar.getInstance().getTimeInMillis() - start > timeout) {
                throw new TimeoutException("Timed out while waiting for element to be hidden");
            }
            sleep(100);
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
                throw new TimeoutException("Timed out while waiting for element to be visible");
            }
            sleep(100);
        }
    }

    public void waitUntilVisible() throws TimeoutException {
        waitUntilVisible(DEFAULT_TIMEOUT);
    }

    public WebElement getWrappedElement() {
        if (this.element instanceof RemoteWebElement) return this.element;

        return this;
    }

    // DEMO ability to "decorate" selenium's logic, for instance adding retry logic
    @Override
    public void click() {
        this.element.click();
    }

    @Override
    public void submit() {
        this.element.submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        this.element.sendKeys(charSequences);
    }

    @Override
    public void clear() {
        this.element.clear();
    }

    @Override
    public String getTagName() {
        return this.element.getTagName();
    }

    @Override
    public String getAttribute(String s) {
        return this.element.getAttribute(s);
    }

    @Override
    public boolean isSelected() {
        return this.element.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return this.element.isEnabled();
    }

    @Override
    public String getText() {
        return this.element.getText();
    }

    public List<WebElement> findElements(By by) {
        return this.element.findElements(by);
    }

    public WebElement findElement(By by) {
        return this.element.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return this.element.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return this.element.getLocation();
    }

    @Override
    public Dimension getSize() {
        return this.element.getSize();
    }

    @Override
    public String getCssValue(String s) {
        return this.element.getCssValue(s);
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) this.element).getCoordinates();
    }
}
