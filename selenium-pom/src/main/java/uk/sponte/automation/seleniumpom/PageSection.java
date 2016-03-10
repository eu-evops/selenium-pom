package uk.sponte.automation.seleniumpom;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.proxies.handlers.Refreshable;

import java.util.List;

/**
 * Created by n450777 on 07/04/15.
 */
public abstract class PageSection implements SearchContext, Refreshable {
    private static final Integer DEFAULT_WAIT_TIMEOUT = 10000;
    protected PageElement rootElement;

    private Refreshable parent;

    public boolean isPresent() {
        return rootElement.isPresent();
    }

    public String getText() {
        return rootElement.getText();
    }

    public void waitFor(Integer timeout) {
        this.rootElement.waitFor(timeout);
    }
    
    public void waitFor() {
        this.waitFor(DEFAULT_WAIT_TIMEOUT);
    }

    public void waitUntilGone() {
        this.waitUntilGone(DEFAULT_WAIT_TIMEOUT);
    }

    public void waitUntilGone(Integer timeout) {
        this.rootElement.waitUntilGone(timeout);
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
}