package uk.sponte.automation.seleniumpom.dependencies;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.Stage;
import uk.sponte.automation.seleniumpom.PageFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by evops on 05/02/2016.
 */
public class GuiceDependencyInjector extends AbstractModule implements DependencyInjector {
    private final static Logger LOG = Logger.getLogger(GuiceDependencyInjector.class.getName());
    private Injector injector;
    private HashMap<Class,DependencyFactory> factories = new HashMap<Class, DependencyFactory>();
    protected ArrayList<Module> modules = new ArrayList<Module>();

    private PageFactory pageFactory;

    public GuiceDependencyInjector(PageFactory pageFactory, DependencyFactory... factories) {
        this.pageFactory = pageFactory;
        modules.add(this);

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

    @Provides
    @Singleton
    public PageFactory pageFactory() {
        return this.pageFactory;
    }

    /**
     * Registers dependency factories, can have multiple dependencies in one factory
     * @param factory
     * @param <T>
     */
    public <T> void registerFactory(DependencyFactory<T> factory) {
        Type[] typeParameters = factory.getClass().getGenericInterfaces();

        for (Type typeParameter : typeParameters) {
            // If it's not a generic type move on
            if(!(typeParameter instanceof ParameterizedType)) continue;
            ParameterizedType parameterizedType = (ParameterizedType) typeParameter;

            // Register first generic type
            this.factories.put((Class) parameterizedType.getActualTypeArguments()[0], factory);
        }
    }
}
