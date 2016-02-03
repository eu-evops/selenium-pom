package uk.sponte.automation.seleniumpom.orchestration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by evops on 03/02/2016.
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

        System.out.printf("[%d] Switching to frame (%s)%n", System.identityHashCode(this), frame);
        this.frame = frame;
        driver.switchTo().frame(driver.findElement(frame));
    }

    public void useDefault() {
        if(this.frame == null) return;
        System.out.printf("[%d] Switching to default%n", System.identityHashCode(this));
        this.frame = null;
        driver.switchTo().defaultContent();
    }
}
