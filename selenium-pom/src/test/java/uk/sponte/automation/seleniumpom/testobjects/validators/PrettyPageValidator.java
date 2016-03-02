package uk.sponte.automation.seleniumpom.testobjects.validators;

import uk.sponte.automation.seleniumpom.annotations.Validator;

/**
 * Test page object
 */
public class PrettyPageValidator implements Validator {
    public boolean isValid() {
        return System.getProperty("Pretty", "false").equalsIgnoreCase("true");
    }
}
