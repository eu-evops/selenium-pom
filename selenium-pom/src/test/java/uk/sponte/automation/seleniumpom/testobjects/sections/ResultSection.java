package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.annotations.Section;

import java.util.List;

/**
 * Created by evops on 05/02/2016.
 */
public class ResultSection {

    @FindBy(css = ".result")
    public List<Result> results;
}
