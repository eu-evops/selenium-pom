package uk.sponte.automation.seleniumpom.orchestration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by evops on 03/02/2016.
 *
 * Manages automatic switching of WebDriver frames in Selenium POM.
 */
public class WebDriverFrameSwitchingOrchestrator {
    private By frame;
    private WebDriver driver;

    public WebDriverFrameSwitchingOrchestrator(WebDriver driver) {
        this.driver = driver;
        this.frame = null;
    }

    public void useFrame(By frame) {
        if(this.frame != null && this.frame.equals(frame)) return;

        this.frame = frame;
        driver.switchTo().frame(driver.findElement(frame));
    }

    public void useDefault() {
        if(this.frame == null) return;

        this.frame = null;
        driver.switchTo().defaultContent();
    }
}
