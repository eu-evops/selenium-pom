package uk.sponte.automation.seleniumpom.testobjects.pages;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.annotations.PageFilter;
import uk.sponte.automation.seleniumpom.testobjects.validators.MobilePageValidator;
import uk.sponte.automation.seleniumpom.testobjects.validators.PrettyPageValidator;

/**
 * Test page object
 */
@PageFilter({MobilePageValidator.class, PrettyPageValidator.class})
public class ZPrettyAndMobileHomepageImpl extends Homepage {
    @FindBy(id = "zpretty and mobile something")
    public PageElement something;
}
