package uk.sponte.automation.web.dependencies;

/**
 * Created by n450777 on 07/04/15.
 */
public class InjectionError extends Error {

    public InjectionError(Throwable cause) {
        super(cause);
    }

    public InjectionError(String message, Throwable cause) {
        super(message, cause);
    }
}
