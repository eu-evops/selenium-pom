package uk.sponte.automation.seleniumpom.dependencies;

/**
 * Created by n450777 on 07/04/15.
 */
public interface DependencyInjector {
    <T> T get(Class<T> klass) throws InjectionError;
    void injectMembers(Object o);
    <T> void registerFactory(DependencyFactory<T> factory);
}
