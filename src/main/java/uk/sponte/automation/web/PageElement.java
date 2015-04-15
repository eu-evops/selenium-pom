package uk.sponte.automation.web;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import uk.sponte.automation.web.proxies.handlers.DynamicHandler;

/**
 * Created by swozniak on 03/04/15.
 */
public interface PageElement extends WebElement, Locatable, WebElementExtensions, WrapsElement, SearchContext, DynamicHandler {
}
