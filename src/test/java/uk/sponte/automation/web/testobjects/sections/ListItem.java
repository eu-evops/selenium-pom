package uk.sponte.automation.web.testobjects.sections;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.web.PageSection;
import uk.sponte.automation.web.annotations.Section;

import java.util.List;

/**
 * Created by n450777 on 13/04/15.
 */
public class ListItem extends PageSection {
    @Section @FindBy(className = "listSubItem") public List<ListSubItem> subItems;
}
