package uk.sponte.automation.seleniumpom.dependencies;

import uk.sponte.automation.seleniumpom.PageFactory;

/**
 * Created by n450777 on 04/03/2016.
 */
public class InternalGuiceDependencyInjector extends GuiceDependencyInjector {
    private PageFactory pageFactory;
    private final DependencyInjector dependencyInjector;

    public InternalGuiceDependencyInjector(
            PageFactory pageFactory,
            DependencyInjector dependencyInjector,
            DependencyFactory... factories) {
        super(pageFactory, factories);
        modules.add(new FieldInitialiserDependencyModule());

        this.pageFactory = pageFactory;
        this.dependencyInjector = dependencyInjector;
    }

    @Override
    protected void configure() {
        super.configure();
        bind(DependencyInjector.class).toInstance(dependencyInjector);
    }

}
