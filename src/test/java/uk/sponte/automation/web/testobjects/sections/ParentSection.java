package uk.sponte.automation.web.testobjects.sections;

import uk.sponte.automation.web.PageSection;
import uk.sponte.automation.web.annotations.Section;

/**
 * Created by n450777 on 07/04/15.
 */
public class ParentSection extends PageSection {
    @Section
    public ChildSection child;
}
