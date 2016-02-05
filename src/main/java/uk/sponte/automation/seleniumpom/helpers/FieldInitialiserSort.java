package uk.sponte.automation.seleniumpom.helpers;

import uk.sponte.automation.seleniumpom.FieldInitialiser;

import java.util.Comparator;

/**
 * Created by evops on 05/02/2016.
 */
public class FieldInitialiserSort implements java.util.Comparator<Class<? extends uk.sponte.automation.seleniumpom.FieldInitialiser>> {
    @Override
    public int compare(Class<? extends FieldInitialiser> aClass, Class<? extends FieldInitialiser> t1) {
        return aClass.getSimpleName().compareTo(t1.getSimpleName());
    }
}
