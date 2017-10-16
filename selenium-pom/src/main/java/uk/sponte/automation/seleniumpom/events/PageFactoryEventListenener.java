package uk.sponte.automation.seleniumpom.events;

import org.openqa.selenium.WebDriver;

public interface PageFactoryEventListenener {
    void pageRefreshed(WebDriver driver);
}
