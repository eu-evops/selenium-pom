package uk.sponte.automation.seleniumpom.dependencies;

import org.openqa.selenium.WebDriver;

import java.util.HashMap;

/**
 * Created by n450777 on 07/04/15.
 */
public class DefaultDependencyInjectorImpl implements DependencyInjector {
    private final HashMap<Class, DependencyFactory> factoryMapping;

    public DefaultDependencyInjectorImpl() {
        factoryMapping = new HashMap<Class, DependencyFactory>();
        factoryMapping.put(WebDriver.class, new WebDriverFactory());
    }

    @Override
    public <T> T get(Class<T> klass) {
        try {
            if(factoryMapping.containsKey(klass))
                return (T) factoryMapping.get(klass).get();

            return klass.newInstance();
        } catch (InstantiationException e) {
            throw new InjectionError(e.getCause());
        } catch(IllegalAccessException e) {
            throw new InjectionError(e.getCause());
        }
    }

    public <T> void registerFactory(Class<T> itemClass, DependencyFactory factory) {
        factoryMapping.put(itemClass, factory);
    }
}
