package uk.sponte.automation.seleniumpom.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Stage;
import com.google.inject.matcher.Matchers;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.InjectionError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by n450777 on 20/04/15.
 */
public class SeleniumPomGuiceModule extends AbstractModule
        implements DependencyInjector {
    private Injector injector;

    private HashMap<Class,DependencyFactory> factories = new HashMap<Class, DependencyFactory>();
    private PageFactory pageFactory;

    private ArrayList<Module> modules = new ArrayList<Module>();

    public SeleniumPomGuiceModule(Module... modules) {
        this.modules.add(this);
        Collections.addAll(this.modules, modules);
    }

    @Override
    protected void configure() {
        // PageObjectModelTypeListener interrogates each type to see if it's a page object.
        // It does this by looking at field types, if it's find any of PageElement,
        bindListener(Matchers.any(), new PageObjectModelTypeListener());
    }

    @Provides
    PageFactory providePageFactory() {
        if(pageFactory != null) return pageFactory;

        pageFactory = new PageFactory(this);
        return pageFactory;
    }

    public Injector getInjector() {
        if (this.injector == null)
            injector = Guice.createInjector(
                    Stage.PRODUCTION,
                    this.modules
            );

        return injector;
    }

    @Override
    public <T> T get(Class<T> aClass) throws InjectionError {
        return getInjector().getInstance(aClass);
    }

    public void injectMembers(Object object) {
        getInjector().injectMembers(object);
    }
}
