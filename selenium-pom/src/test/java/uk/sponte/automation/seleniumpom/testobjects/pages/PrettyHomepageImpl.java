package uk.sponte.automation.seleniumpom.testobjects.pages;

import uk.sponte.automation.seleniumpom.annotations.PageFilter;
import uk.sponte.automation.seleniumpom.testobjects.validators.PrettyPageValidator;

/**
 * Test page object
 */
@PageFilter(PrettyPageValidator.class)
public class PrettyHomepageImpl extends Homepage {
}
