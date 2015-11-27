package uk.sponte.automation.seleniumpom.tests.ui;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by n450777 on 27/11/2015.
 * Tests for type converter
 */
public class TypeConverterTests extends BasePageTest {

    @Test
    public void canConvertToInteger() {
        assertEquals(222, testPage.numberOfUsers.intValue());
    }
}
