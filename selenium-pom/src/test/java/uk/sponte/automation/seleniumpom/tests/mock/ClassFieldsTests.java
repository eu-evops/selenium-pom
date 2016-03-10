package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.Test;
import uk.sponte.automation.seleniumpom.helpers.ClassHelper;
import uk.sponte.automation.seleniumpom.testobjects.pages.ZPrettyAndMobileHomepageImpl;

import java.lang.reflect.Field;

/**
 * Created by n450777 on 08/03/2016.
 */
public class ClassFieldsTests {

    @Test
    public void getAllFields() {
        Iterable<Field> fieldsFromClass = ClassHelper
                .getFieldsFromClass(ZPrettyAndMobileHomepageImpl.class);

        System.out.printf("%s%n", fieldsFromClass);
    }

}
