package uk.sponte.automation.seleniumpom.fieldInitialisers;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.helpers.FrameWrapper;
import uk.sponte.automation.seleniumpom.orchestration.WebDriverFrameSwitchingOrchestrator;

import java.lang.reflect.Field;

/**
 * Created by evops on 02/02/2016.
 */
public interface FieldInitialiser {
    Boolean initialiseField(
            Field field,
            Object page,
            SearchContext searchContext,
            FrameWrapper frame
    );
}
