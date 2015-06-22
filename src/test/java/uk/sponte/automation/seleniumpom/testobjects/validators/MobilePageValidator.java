package uk.sponte.automation.seleniumpom.testobjects.validators;

import uk.sponte.automation.seleniumpom.annotations.Validator;

/**
 * Test page object
 */
public class MobilePageValidator implements Validator {
    public boolean isValid() {
        return System.getProperty("Mobile", "false").equalsIgnoreCase("true");
    }
}
