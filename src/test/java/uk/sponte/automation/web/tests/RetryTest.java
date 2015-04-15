package uk.sponte.automation.web.tests;

import org.junit.Test;
import uk.sponte.automation.web.helpers.OperationHelper;

import java.util.NoSuchElementException;

/**
 * Created by n450777 on 13/04/15.
 */
public class RetryTest {
    @Test
    public void canRetry() {
        OperationHelper.withRetry(3, () -> {
            throw new NoSuchElementException("lol");
        });
    }
}
