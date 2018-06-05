package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.proxies.handlers.Refreshable;

import java.util.List;

/**
 * Created by n450777 on 07/04/15.
 */
public abstract class PageSection implements SearchContext, Refreshable, WebElementExtensions {
    private static final Integer DEFAULT_WAIT_TIMEOUT = 10000;
    protected PageElement rootElement;

    /**
     * @return text from value attribute of an element
     */
    @Override
    public String getValue() {
        return this.rootElement.getValue();
    }

    /**
     * Performs clear() sendKeys() action on a given element
     *
     * @param value Text value to enter into a text field
     */
    @Override
    public void set(String value) {
        this.rootElement.set(value);
    }

    /**
     * Gets text from a hidden element
     *
     * @return String
     */
    @Override
    public String getHiddenText() {
        return this.rootElement.getHiddenText();
    }

    /**
     * Performs clear() sendKeys() action on a given element
     *
     * @param format Text format to enter into a text field
     * @param args Arguments used in the format string
     */
    @Override
    public void set(String format, Object... args) {
        this.rootElement.set(format, args);
    }

    /**
     * Double clicks on an element
     */
    @Override
    public void doubleClick() {
        this.rootElement.doubleClick();
    }

    /**
     * Drags an element onto a given web element
     *
     * @param pageElement element to drop element onto
     */
    @Override
    public void dropOnto(PageElement pageElement) {
        this.rootElement.dropOnto(pageElement);
    }

    /**
     * Waits for element to be available in DOM
     *
     * @param timeout in seconds
     */
    @Override
    public PageElement waitFor(Integer timeout) {
        return this.rootElement.waitFor(timeout);
    }

    @Override
    public PageElement waitFor() {
        return this.rootElement.waitFor();
    }

    /**
     * Waits for element to be hidden (display: none)
     *
     * @param timeout amount of milliseconds to timeout after
     */
    @Override
    public PageElement waitUntilHidden(Integer timeout) {
        return this.rootElement.waitUntilHidden(timeout);
    }

    @Override
    public PageElement waitUntilHidden() {
        return waitUntilHidden(DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Waits until element is visible on the page
     *
     * @param timeout amount of milliseconds to timeout after
     */
    @Override
    public PageElement waitUntilVisible(Integer timeout) {
        return this.rootElement.waitUntilVisible(timeout);
    }

    @Override
    public PageElement waitUntilVisible() {
        return this.rootElement.waitUntilVisible();
    }

    private Refreshable parent;

    public boolean isPresent() {
        return rootElement.isPresent();
    }

    public String getText() {
        return rootElement.getText();
    }

    public void waitUntilGone() {
        this.waitUntilGone(DEFAULT_WAIT_TIMEOUT);
    }

    public void waitUntilGone(Integer timeout) {
        this.rootElement.waitUntilGone(timeout);
    }

    @Override
    public PageElement waitUntilClickable() {
        return this.rootElement.waitUntilClickable();
    }

    @Override
    public PageElement waitUntilClickable(Integer timeout) {
        return this.rootElement.waitUntilClickable(timeout);
    }

    /**
     * Waits until element's location does not change between intervals
     */
    @Override
    public PageElement waitUntilStopsMoving() {
        return rootElement.waitUntilStopsMoving();
    }

    /**
     * Waits until element's location does not change between intervals
     *
     * @param timeout in seconds
     */
    @Override
    public PageElement waitUntilStopsMoving(Integer timeout) {
        return this.rootElement.waitUntilStopsMoving(timeout);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return rootElement.findElements(by);
    }

    @Override
    public void setParent(Refreshable refreshable) {
        this.parent = refreshable;
    }

    @Override
    public WebElement findElement(By by) {
        return rootElement.findElement(by);
    }

    @Override
    public void invalidate() {
        this.rootElement.invalidate();
    }

    @Override
    public void refresh() { this.rootElement.refresh(); }

    @Override
    public void pageRefreshed(WebDriver driver) {
        invalidate();
    }
}