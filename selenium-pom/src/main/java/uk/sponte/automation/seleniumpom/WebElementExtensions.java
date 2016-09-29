package uk.sponte.automation.seleniumpom;

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
     * Gets text from a hidden element
     * @return String
     */
    String getHiddenText();

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
     */
    PageElement waitFor(Integer timeout);
    PageElement waitFor();

    /**
     * Waits for element to not be available in DOM
     */
    void waitUntilGone(Integer timeout);
    void waitUntilGone();

    /**
     * Waits for element to be hidden (display: none)
     * @param timeout amount of milliseconds to timeout after
     */
    PageElement waitUntilHidden(Integer timeout);
    PageElement waitUntilHidden();

    /**
     * Waits until element is visible on the page
     * @param timeout amount of milliseconds to timeout after
     */
    PageElement waitUntilVisible(Integer timeout);
    PageElement waitUntilVisible();

    /**
     * Waits until element's location does not change between intervals
     */
    PageElement waitUntilStopsMoving(Integer timeout);
    PageElement waitUntilStopsMoving();
}
