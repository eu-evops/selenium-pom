package uk.sponte.automation.seleniumpom.proxies.handlers;

import uk.sponte.automation.seleniumpom.events.PageFactoryEventListenener;

/**
 * Created by n450777 on 04/03/2016.
 */
public interface Refreshable extends PageFactoryEventListenener {
    void invalidate();
    void refresh();
    void setParent(Refreshable refreshable);
}
