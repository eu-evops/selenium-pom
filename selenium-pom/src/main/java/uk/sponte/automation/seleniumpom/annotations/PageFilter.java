package uk.sponte.automation.seleniumpom.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
* PageFilter annotation - used to annotate custom / non standard page objects, used
 * in page factory to find best implementation
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PageFilter {
    Class<? extends Validator>[] value();
}
