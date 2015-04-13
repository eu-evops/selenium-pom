package uk.sponte.automation.web;

import java.util.concurrent.TimeoutException;

/**
 * Created by n450777 on 07/04/15.
 */
public abstract class PageSection {
    private static final Integer DEFAULT_WAIT_TIMEOUT = 10000;
    protected PageElement rootElement;

    public boolean isPresent() {
        return rootElement.isPresent();
    }

    public void waitFor(Integer timeout) throws TimeoutException {
        this.rootElement.waitFor(timeout);
    }
    
    public void waitFor() throws TimeoutException {
        this.waitFor(DEFAULT_WAIT_TIMEOUT);
    }
}