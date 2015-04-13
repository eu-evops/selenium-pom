package uk.sponte.automation.web;

import org.openqa.selenium.SearchContext;

/**
 * Created by n450777 on 07/04/15.
 */
public interface Page extends ElementContainer {
    SearchContext getSearchContext();
}
