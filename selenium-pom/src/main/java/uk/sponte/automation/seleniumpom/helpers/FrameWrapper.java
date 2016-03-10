package uk.sponte.automation.seleniumpom.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.annotations.Frame;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by evops on 05/02/2016.
 */
public class FrameWrapper {
    private final static Logger LOG = Logger.getLogger(Frame.class.getName());

    private WebDriver driver;
    private FrameWrapper parent;
    public By frameBy;

    public FrameWrapper(WebDriver driver, By frameBy) {
        this.driver = driver;
        this.frameBy = frameBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrameWrapper)) return false;

        FrameWrapper that = (FrameWrapper) o;

        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        if(frameBy == null) return 0;

        int result = this.frameBy.toString().hashCode();
        if(parent != null)
            result += parent.hashCode();

        result = 31 * result;
        return result;
    }

    public void use() {
        if(this.parent != null) {
            this.parent.use();
        }

        LOG.log(Level.FINE, "Switching to frame {0}", this);
        WebElement frameElement = driver.findElement(this.frameBy);
        driver.switchTo().frame(frameElement);
    }

    public FrameWrapper setParent(FrameWrapper parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public String toString() {
        ArrayList<String> frameWrappers = new ArrayList<String>();
        FrameWrapper frame = this;
        while(frame != null) {
            frameWrappers.add(frame.frameBy.toString());
            frame = frame.parent;
        }

        return ArrayHelper.join(frameWrappers, " inside -> ");
    }
}
