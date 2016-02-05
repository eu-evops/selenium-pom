package uk.sponte.automation.seleniumpom.orchestration;

import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by evops on 03/02/2016.
 *
 * Manages automatic switching of WebDriver frames in Selenium POM.
 */
public class WebDriverFrameSwitchingOrchestrator {
    private final static Logger LOG = Logger.getLogger(WebDriverFrameSwitchingOrchestrator.class.getName());

    private FrameWrapper frame;
    private WebDriver driver;

    public WebDriverFrameSwitchingOrchestrator(WebDriver driver) {
        this.driver = driver;
        this.frame = null;
    }

    public void useFrame(FrameWrapper frame) {
        if(this.frame != null && this.frame.equals(frame)) return;

        useDefault(true);
        LOG.log(Level.INFO, "Switching to frame {0} ({1})", new Object[] { frame, frame.hashCode() });

        this.frame = frame;
        frame.use();
    }

    public void useDefault() {this.useDefault(false);}

    private void useDefault(boolean force) {
        if(!force && this.frame == null) return;

        LOG.log(Level.INFO, "Switching to default content");
        this.frame = null;
        driver.switchTo().defaultContent();
    }
}
