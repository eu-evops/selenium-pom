package uk.sponte.automation.web.dependencies;

/**
 * Created by n450777 on 07/04/15.
 */
public interface DependencyFactory<E> {
    <E> E get();
}
