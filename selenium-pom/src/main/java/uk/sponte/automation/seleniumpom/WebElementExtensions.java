package uk.sponte.automation.seleniumpom;

import java.util.concurrent.TimeoutException;

/**
 * Created by swozniak on 03/04/15.
 */
public interface WebElementExtensions {
    /**
     *
     * @return true if element is present
     */
    boolean isPresent();

    /**
     *
     * @return text from value attribute of an element
     */
    String getValue();

    /**
     * Performs clear() sendKeys() action on a given element
     * @param value Text value to enter into a text field
     */
    void set(String value);

    /**
     * Performs clear() sendKeys() action on a given element
     * @param format Text format to enter into a text field
     */
    void set(String format, Object... args);

    /**
     * Double clicks on an element
     */
    void doubleClick();

    /**
     * Drags an element onto a given web element
     * @param pageElement element to drop element onto
     */
    void dropOnto(PageElement pageElement);

    /**
     * Waits for element to be available in DOM
     * @throws TimeoutException
     */
    PageElement waitFor(Integer timeout) throws TimeoutException;
    PageElement waitFor() throws TimeoutException;

    /**
     * Waits for element to not be available in DOM
     * @throws TimeoutException
     */
    void waitUntilGone(Integer timeout) throws TimeoutException;
    void waitUntilGone() throws TimeoutException;

    /**
     * Waits for element to be hidden (display: none)
     * @param timeout amount of milliseconds to timeout after
     * @throws TimeoutException
     */
    PageElement waitUntilHidden(Integer timeout) throws TimeoutException;
    PageElement waitUntilHidden() throws TimeoutException;

    /**
     * Waits until element is visible on the page
     * @param timeout amount of milliseconds to timeout after
     * @throws TimeoutException
     */
    PageElement waitUntilVisible(Integer timeout) throws TimeoutException;
    PageElement waitUntilVisible() throws TimeoutException;
}
