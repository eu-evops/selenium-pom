package uk.sponte.automation.seleniumpom.proxies.handlers;

/**
 * Created by n450777 on 04/03/2016.
 */
public interface Refreshable {
    void invalidate();
    void refresh();
    void setParent(Refreshable refreshable);
}
