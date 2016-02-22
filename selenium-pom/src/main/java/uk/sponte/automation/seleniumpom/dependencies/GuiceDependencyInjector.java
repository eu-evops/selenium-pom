package uk.sponte.automation.seleniumpom.dependencies;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by evops on 05/02/2016.
 */
public class GuiceDependencyInjector extends AbstractModule implements DependencyInjector {
    private Injector injector;
    private HashMap<Class,DependencyFactory> factories = new HashMap<Class, DependencyFactory>();
    private DependencyInjector externalDependencyInjector;
    private ArrayList<Module> modules = new ArrayList<Module>();

    public GuiceDependencyInjector(DependencyFactory... factories) {
        this(null, factories);
    }

    public GuiceDependencyInjector(DependencyInjector dependencyInjector, DependencyFactory... factories) {
        modules.add(this);

        if(dependencyInjector != null) {
            externalDependencyInjector = dependencyInjector;
            modules.add(new FieldInitialiserDependencyModule());
        }

        for (DependencyFactory factory : factories) {
            registerFactory(factory);
        }
    }

    private Injector getInjector() {
        if(this.injector != null) return injector;

        injector = Guice.createInjector(Stage.PRODUCTION, modules);
        return injector;
    }

    @Override
    protected void configure() {
        if(externalDependencyInjector != null) {
            bind(DependencyInjector.class).toInstance(externalDependencyInjector);
        }

        // Register custom factories
        for (Type type : this.factories.keySet()) {
            bind(((Class<Object>)type)).toProvider(this.factories.get(type));
        }
    }

    @Override
    public <T> T get(Class<T> klass) throws InjectionError {
        return getInjector().getInstance(klass);
    }

    public void injectMembers(Object object) {
        getInjector().injectMembers(object);
    }

    public <T> void registerFactory(DependencyFactory<T> factory) {
        Type[] typeParameters = factory.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) typeParameters[0];
        this.factories.put((Class) parameterizedType.getActualTypeArguments()[0], factory);
    }
}
