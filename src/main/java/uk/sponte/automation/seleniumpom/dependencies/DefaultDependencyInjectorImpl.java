package uk.sponte.automation.seleniumpom.dependencies;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by n450777 on 07/04/15.
 * Default implementation of DependencyInjector - this is very basic
 * and in most cases it should be replaced by something more sofisticated
 * such as google guice or spring for instance.
 */
public class DefaultDependencyInjectorImpl implements DependencyInjector {
    private final HashMap<Type, DependencyFactory<?>> factoryMapping = new HashMap<Type, DependencyFactory<?>>();

    public DefaultDependencyInjectorImpl() {
        registerFactory(new WebDriverFactory());
    }

    public <T> void registerFactory(DependencyFactory<T> factory) {
        Class klass = factory.getClass();
        Type type = klass.getGenericInterfaces()[0];

        if(!ParameterizedType.class.isAssignableFrom(type.getClass())) {
            throw new IllegalArgumentException("Factory needs to implement DependencyFactory<T>");
        }

        ParameterizedType parameterizedType = (ParameterizedType)type;
        Type[] typeArguments = parameterizedType
                .getActualTypeArguments();

        Type typeArgument = typeArguments[0];
        this.factoryMapping.put(typeArgument, factory);
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
