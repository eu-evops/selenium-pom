package uk.sponte.automation.seleniumpom.helpers;

import org.reflections.Reflections;
import uk.sponte.automation.seleniumpom.annotations.PageFilter;
import uk.sponte.automation.seleniumpom.annotations.Validator;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;

import java.util.*;

/**
 * Helper class used to find best matching implementation (child class) based on
 * PageFilter and Validator(s)
 */
public class ImplementationFinder<T> {
    private Class<T> pageClass;
    private DependencyInjector dependencyInjector;

    private static HashMap<String, Reflections> reflectionsCache = new HashMap<String, Reflections>();

    public ImplementationFinder(Class<T> pageClass, DependencyInjector dependencyInjector) {
        this.pageClass = pageClass;
        this.dependencyInjector = dependencyInjector;
    }

    public T find() {
        Set<Class<? extends T>> subTypesOf = getReflections(pageClass).getSubTypesOf(pageClass);

        ArrayList<Class<? extends T>> sortedListOfImplementationClasses = new ArrayList<Class<? extends T>>(subTypesOf);
        Collections.sort(sortedListOfImplementationClasses, new PageFilterAnnotatedClassComparator());

        for (Class<? extends T> klass : subTypesOf) {
            PageFilter annotation = klass.getAnnotation(PageFilter.class);
            if (annotation != null) {
                boolean valid = true;
                for (Class<? extends Validator> validatorClass : annotation.value()) {
                    Validator validator = dependencyInjector.get(validatorClass);
                    if (!validator.isValid()) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    return dependencyInjector.get(klass);
                }
            }
        }

        return dependencyInjector.get(pageClass);

    }

    private Reflections getReflections(Class<T> pageClass) {
        String packageName = pageClass.getPackage().getName();
        if(reflectionsCache.containsKey(packageName)) {
            return reflectionsCache.get(packageName);
        }

        Reflections reflections = new Reflections(pageClass.getPackage());
        reflectionsCache.put(packageName, reflections);

        return reflections;
    }

    private class PageFilterAnnotatedClassComparator implements Comparator<Class<? extends T>> {
        @Override
        public int compare(Class<? extends T> o1, Class<? extends T> o2) {
            int o1ValidatorsCount = 0;
            int o2ValidatorsCount = 0;

            PageFilter pageFilterO1 = o1.getAnnotation(PageFilter.class);
            PageFilter pageFilterO2 = o2.getAnnotation(PageFilter.class);

            if (pageFilterO1 != null)
                o1ValidatorsCount = pageFilterO1.value().length;

            if (pageFilterO2 != null)
                o1ValidatorsCount = pageFilterO2.value().length;

            return Integer.compare(o1ValidatorsCount, o2ValidatorsCount);
        }
    }
}
