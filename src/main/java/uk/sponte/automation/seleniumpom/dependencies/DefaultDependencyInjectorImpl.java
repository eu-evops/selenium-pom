package uk.sponte.automation.seleniumpom.dependencies;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;

/**
 * Created by n450777 on 07/04/15.
 */
public class DefaultDependencyInjectorImpl implements DependencyInjector {
    private final HashMap<Class<?>, DependencyFactory<?>> factoryMapping = new HashMap<Class<?>, DependencyFactory<?>>();

    public DefaultDependencyInjectorImpl() {
        registerFactory(WebDriver.class, new WebDriverFactory());
    }

    public <T> void registerFactory(Class<T> klass, DependencyFactory<T> factory) {
        this.factoryMapping.put(klass, factory);
    }

    @Override
    public <T> T get(Class<T> klass) {
        try {
            if(factoryMapping.containsKey(klass)) {
                Object objectInstance = factoryMapping.get(klass).get();
                return klass.cast(objectInstance);
            }

            return klass.newInstance();
        } catch (InstantiationException e) {
            throw new InjectionError(e.getCause());
        } catch(IllegalAccessException e) {
            throw new InjectionError(e.getCause());
        }
    }
}
