package uk.sponte.automation.seleniumpom.helpers;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by n450777 on 30/04/15.
 */
public class ClassHelper {
    public static Iterable<Field> getFieldsFromClass(Class<?> startClass) {
        List<Field> currentClassFields = new ArrayList<Field>();
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null) {
            List<Field> parentClassFields =
                    (List<Field>) getFieldsFromClass(parentClass);
            currentClassFields.addAll(parentClassFields);
        }

        currentClassFields.addAll(Lists.newArrayList(startClass.getDeclaredFields()));
        return currentClassFields;
    }
}
