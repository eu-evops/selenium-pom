package uk.sponte.automation.seleniumpom.dependencies;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import uk.sponte.automation.seleniumpom.fieldInitialisers.FieldInitialiser;
import uk.sponte.automation.seleniumpom.helpers.FieldInitialiserSort;
import uk.sponte.automation.seleniumpom.helpers.SortingHelper;

import java.util.*;

/**
 * Created by evops on 05/02/2016.
 */
public class FieldInitialiserDependencyModule extends AbstractModule {

    private Reflections reflections;

    @Override
    protected void configure() {
        Multibinder<FieldInitialiser> fieldInitialiserMultibinder = Multibinder.newSetBinder(binder(), FieldInitialiser.class);
        Set<Class<? extends FieldInitialiser>> fieldInitialisers = getReflections().getSubTypesOf(FieldInitialiser.class);
        List<Class<? extends FieldInitialiser>> sortedInitialisers = SortingHelper.asSortedList(fieldInitialisers, new FieldInitialiserSort());
        for (Class<? extends FieldInitialiser> fieldInitialiserClass : sortedInitialisers) {
            fieldInitialiserMultibinder.addBinding().to(fieldInitialiserClass).in(Singleton.class);
        }
    }

    private Reflections getReflections() {
        if(reflections != null) return reflections;

        String packageName = FieldInitialiser.class.getPackage().getName();

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setScanners(new SubTypesScanner());
        configurationBuilder.filterInputsBy(new FilterBuilder().includePackage(packageName));
        configurationBuilder.setUrls(ClasspathHelper.forPackage(packageName));

        reflections = new Reflections(configurationBuilder);
        return reflections;
    }

}
