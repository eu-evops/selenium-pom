package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import uk.sponte.automation.seleniumpom.proxies.handlers.DynamicHandler;
import uk.sponte.automation.seleniumpom.proxies.handlers.Refreshable;

/**
 * Created by swozniak on 03/04/15.
 */
public interface PageElement extends WebElement, Locatable, WebElementExtensions, WrapsElement, SearchContext, DynamicHandler,
        Refreshable {
    boolean isPresent();

    String getHiddenText();

    String getValue();

    void set(String text);

    void set(String format, Object... args);

    // DEMO custom actions made easier
    void doubleClick();

    void dropOnto(PageElement target);

    PageElement waitFor(Integer timeout);

    PageElement waitFor();

    void waitUntilGone(Integer timeout);

    void waitUntilGone();

    // DEMO waiting for things to happen, for instance waiting for element to disappear from the page
    PageElement waitUntilHidden(Integer timeout);

    PageElement waitUntilHidden();

    PageElement waitUntilVisible(Integer timeout);

    PageElement waitUntilVisible();
}
