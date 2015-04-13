package uk.sponte.automation.web.dependencies;

/**
 * Created by n450777 on 07/04/15.
 */
public interface DependencyInjector {
    <T> T get(Class<T> klass) throws InjectionError;
}
