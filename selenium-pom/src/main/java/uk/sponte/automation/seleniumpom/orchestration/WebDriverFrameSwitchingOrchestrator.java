package uk.sponte.automation.seleniumpom.orchestration;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by evops on 03/02/2016.
 *
 * Manages automatic switching of WebDriver frames in Selenium POM.
 */
@Singleton
public class WebDriverFrameSwitchingOrchestrator {
    private final static Logger LOG = Logger.getLogger(WebDriverFrameSwitchingOrchestrator.class.getName());

    private FrameWrapper frame;
    @Inject DependencyInjector dependencyInjector;

    public void useFrame(FrameWrapper frame) {
        LOG.log(Level.FINE, "Called use frame with {0}", frame);
        if(frame == null) {
            useDefault();
            return;
        }

        if(this.frame != null && this.frame.equals(frame)) return;

        useDefault(true);

        this.frame = frame;
        frame.use();
    }

    private void useDefault() {this.useDefault(false);}

    private void useDefault(boolean force) {
        if(!force && this.frame == null) return;

        this.frame = null;
        LOG.log(Level.FINE, "Switching to default content");
        dependencyInjector.get(WebDriver.class).switchTo().defaultContent();
    }
}
