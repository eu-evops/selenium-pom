package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.annotations.Section;

/**
 * Created by n450777 on 13/04/15.
 */
public class PlainSection {
    @FindBy(id = "plainSectionChild")
    @Section public ChildSection childInheritingFromPageSection;
}
