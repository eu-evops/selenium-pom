package uk.sponte.automation.seleniumpom.tests;

import org.junit.Test;
import uk.sponte.automation.seleniumpom.exceptions.RetryException;
import uk.sponte.automation.seleniumpom.helpers.OperationHelper;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

/**
 * Created by n450777 on 13/04/15.
 */
public class RetryTest {
    @Test
    public void canRetry() {
        final int[] times = {0};
        try {
            OperationHelper.withRetry(3, 50, new Runnable() {
                @Override
                public void run() {
                    times[0]++;
                    throw new NoSuchElementException("lol");
                }
            });
        } catch(RetryException e) {
            // nothing
        }

        assertEquals(3, times[0]);
    }
}
