package uk.sponte.automation.seleniumpom.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageSection;
import uk.sponte.automation.seleniumpom.annotations.Section;

import java.util.List;

/**
 * Created by n450777 on 13/04/15.
 */
public class ListItem extends PageSection {
    @Section @FindBy(className = "listSubItem") public List<ListSubItem> subItems;
}
