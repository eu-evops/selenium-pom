package uk.sponte.automation.seleniumpom.testobjects.sections;

import uk.sponte.automation.seleniumpom.PageSection;
import uk.sponte.automation.seleniumpom.annotations.Section;

/**
 * Created by n450777 on 07/04/15.
 */
public class ParentSection extends PageSection {
    @Section
    public ChildSection child;
}
