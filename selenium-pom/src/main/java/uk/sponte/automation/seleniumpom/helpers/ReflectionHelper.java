package uk.sponte.automation.seleniumpom.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by n450777 on 04/03/2016.
 */
public class ReflectionHelper {

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getField(object, fieldName);
        if (field == null)
            return null;

        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static List<Field> getAllFields(Object object) {
        return getAllClassFields(object.getClass());
    }

    public static List<Field> getAllClassFields(Class klass) {
        ArrayList<Field> fields = new ArrayList<Field>();
        while (klass != null) {
            Collections.addAll(fields, klass.getDeclaredFields());
            klass = klass.getSuperclass();
        }

        return fields;
    }

    public static Field getField(Object object, String fieldName) {
        return getField(object.getClass(), fieldName);
    }

    public static Field getField(Class klass, String fieldName) {
        while (klass != null) {
            try {
                Field field = klass.getDeclaredField(fieldName);
                return field;

            } catch (NoSuchFieldException e) {
                klass = klass.getSuperclass();
            }
        }

        return null;
    }

    public static boolean isWrapperType(Class<?> clazz) {
        return getWrapperTypes().contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }
}
