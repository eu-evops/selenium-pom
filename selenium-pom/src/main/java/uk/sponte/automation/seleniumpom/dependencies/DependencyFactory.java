package uk.sponte.automation.seleniumpom.dependencies;

import com.google.inject.Provider;

/**
 * Created by n450777 on 07/04/15.
 */
public interface DependencyFactory<T> extends Provider<T> {
    T get();
}