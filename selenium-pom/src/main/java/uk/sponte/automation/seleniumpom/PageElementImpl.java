package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.sponte.automation.seleniumpom.proxies.handlers.Refreshable;
import uk.sponte.automation.seleniumpom.proxies.handlers.WebElementHandler;
import uk.sponte.automation.seleniumpom.webdriverConditions.ElementLocationStaticCondition;
import uk.sponte.automation.seleniumpom.webdriverConditions.ElementPresentCondition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;


/**
 * Thin wrapper around selenium WebElement. It also adds
 * extension methods to simplify interacting with elements
 * in HTML.
 * Created by swozniak on 03/04/15.
 */
public class PageElementImpl implements PageElement {
    private final static Integer DEFAULT_TIMEOUT = 5000;

    private WebElement webElement;
    private Refreshable parent;

    public PageElementImpl(WebElement webElement) {
        this.webElement = webElement;
    }

    public boolean canHandle(Method methodName) {
        for (Method method : this.getClass().getMethods()) {
            if (method.getName().equals(methodName.getName()))
                return true;
        }

        return false;
    }

    @Override
    public boolean isPresent() {
        try {
            this.webElement.getTagName();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }

        return true;
    }

    @Override
    public String getHiddenText() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        assert jsExecutor != null;

        return (String) jsExecutor.executeScript(
                "return arguments[0].innerText;", webElement);
    }

    @Override
    public String getValue() {
        return this.webElement.getAttribute("value");
    }

    @Override
    public void set(String text) {
        String tagName = this.webElement.getTagName();
        if (tagName.equalsIgnoreCase("input")) {
            this.webElement.clear();
            this.webElement.sendKeys(text);
            return;
        }

        if (tagName.equalsIgnoreCase("select")) {
            Select select = new Select(this.webElement);
            select.selectByValue(text);
            return;
        }

        throw new Error("Cannot set elements value: " + tagName);
    }

    @Override
    public void set(String format, Object... args) {
        this.set(String.format(format, args));
    }


    // DEMO custom actions made easier
    @Override
    public void doubleClick() {
        WebDriver driver = getDriver();
        assert(driver != null);

        new Actions(driver).doubleClick(this.webElement).perform();
    }

    @Override
    public void dropOnto(PageElement target) {
        WebDriver driver = getDriver();
        assert(driver != null);

        new Actions(driver).dragAndDrop(this.webElement, target).perform();
    }

    @Override
    public PageElement waitFor(Integer timeout) {

        final ElementPresentCondition condition = new ElementPresentCondition(this.webElement);
        getWebDriverWait(timeout).until(condition);
        return this;
    }

    @Override
    public PageElement waitFor() {
        return waitFor(DEFAULT_TIMEOUT);
    }

    @Override
    public void waitUntilGone(Integer timeout)  {
        ElementPresentCondition condition = new ElementPresentCondition(
                webElement);
        getWebDriverWait(timeout).until(not(condition));
    }

    @Override
    public void waitUntilGone() {
        waitUntilGone(DEFAULT_TIMEOUT);
    }

    // DEMO waiting for things to happen, for instance waiting for element to disappear from the page
    @Override
    public PageElement waitUntilHidden(Integer timeout) {
        getWebDriverWait(timeout).until(not(visibilityOf(this.webElement)));
        return this;
    }

    @Override
    public PageElement waitUntilHidden() {
        return waitUntilHidden(DEFAULT_TIMEOUT);
    }

    @Override
    public PageElement waitUntilVisible(Integer timeout) {
        getWebDriverWait(timeout).until(visibilityOf(this.webElement));
        return this;
    }

    @Override
    public PageElement waitUntilVisible() {
        return waitUntilVisible(DEFAULT_TIMEOUT);
    }

    @Override
    public PageElement waitUntilStopsMoving() {
        return waitUntilStopsMoving(DEFAULT_TIMEOUT);
    }

    /**
     * Waits until element's location does not change between intervals
     *
     * @param timeout in seconds
     */
    @Override
    public PageElement waitUntilStopsMoving(Integer timeout) {
        getWebDriverWait(timeout).until(new ElementLocationStaticCondition(this.webElement));
        return this;
    }

    public WebElement getWrappedElement() {
        return this.webElement;
    }

    // DEMO ability to "decorate" selenium's logic, for instance adding retry logic
    @Override
    public void click() {
        this.webElement.click();
    }

    @Override
    public void submit() {
        this.webElement.submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        this.webElement.sendKeys(charSequences);
    }

    @Override
    public void clear() {
        this.webElement.clear();
    }

    @Override
    public String getTagName() {
        return this.webElement.getTagName();
    }

    @Override
    public String getAttribute(String s) {
        return this.webElement.getAttribute(s);
    }

    @Override
    public boolean isSelected() {
        return this.webElement.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return this.webElement.isEnabled();
    }

    @Override
    public String getText() {
        return this.webElement.getText();
    }

    public List<WebElement> findElements(By by) {
        return this.webElement.findElements(by);
    }

    public WebElement findElement(By by) {
        return this.webElement.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return this.webElement.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return this.webElement.getLocation();
    }

    @Override
    public Dimension getSize() {
        return this.webElement.getSize();
    }

    /**
     * @return The location and size of the rendered element
     */
    @Override
    public Rectangle getRect() {
        return this.webElement.getRect();
    }

    @Override
    public String getCssValue(String s) {
        return this.webElement.getCssValue(s);
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) this.webElement).getCoordinates();
    }

    private WebDriverWait getWebDriverWait(long timeout) {
        return getWebDriverWait(timeout, 100);
    }

    private WebDriverWait getWebDriverWait(long timeout, long interval) {
        WebDriver driver = getDriver();
        assert(driver != null);

        return new WebDriverWait(driver, Math.round(timeout / 1000f), interval);
    }

    @Override
    public void invalidate() {
        InvocationHandler invocationHandler = Proxy
                .getInvocationHandler(this.webElement);
        if(invocationHandler != null) {
            if(invocationHandler instanceof Refreshable) {
                ((Refreshable) invocationHandler).invalidate();
            }
        }
    }

    @Override
    public void refresh() {
        if(this.parent != null) parent.refresh();
    }

    @Override
    public void setParent(Refreshable refreshable) {
        this.parent = refreshable;
    }


    private WebDriver getDriver(){
        if(this.webElement instanceof WrapsDriver) {
            return ((WrapsDriver) this.webElement).getWrappedDriver();
        }

        if(this.webElement instanceof Proxy){
            InvocationHandler invocationHandler = Proxy
                    .getInvocationHandler(this.webElement);

            if(invocationHandler instanceof WebElementHandler) {
                return ((WebElementHandler) invocationHandler).getDriver();
            }
        }

        return null;
    }

    /**
     * Capture the screenshot and store it in the specified location.
     * <p>
     * <p>For WebDriver extending TakesScreenshot, this makes a best effort
     * depending on the browser to return the following in order of preference:
     * <ul>
     * <li>Entire page</li>
     * <li>Current window</li>
     * <li>Visible portion of the current frame</li>
     * <li>The screenshot of the entire display containing the browser</li>
     * </ul>
     * <p>
     * <p>For WebElement extending TakesScreenshot, this makes a best effort
     * depending on the browser to return the following in order of preference:
     * - The entire content of the HTML element
     * - The visisble portion of the HTML element
     *
     * @param target target type, @see OutputType
     * @return Object in which is stored information about the screenshot.
     * @throws WebDriverException on failure.
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> target)
            throws WebDriverException {
        return this.webElement.getScreenshotAs(target);
    }

    @Override
    public void pageRefreshed(WebDriver driver) {
        invalidate();
    }
}
