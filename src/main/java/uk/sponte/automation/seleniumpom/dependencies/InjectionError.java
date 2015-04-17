package uk.sponte.automation.seleniumpom.dependencies;

/**
 * Used to throw errors during dependency injection process.
 * Created by n450777 on 07/04/15.
 */
public class InjectionError extends Error {
    public InjectionError(Throwable cause) {
        super(cause);
    }

    public InjectionError(String message) {
        super(message);
    }
}
