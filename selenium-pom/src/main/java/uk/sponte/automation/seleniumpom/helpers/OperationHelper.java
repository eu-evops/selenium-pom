package uk.sponte.automation.seleniumpom.helpers;

import uk.sponte.automation.seleniumpom.exceptions.RetryException;

import java.util.logging.Logger;

/**
 * Created by n450777 on 13/04/15.
 */
public class OperationHelper {
    private final static Logger LOG = Logger.getLogger(OperationHelper.class.getName());
    public static void withRetry(int maxAttempts, Runnable runnable) throws RetryException {
        withRetry(maxAttempts, 500, runnable);
    }

    public static void withRetry(int maxAttempts, int delay, Runnable runnable) throws RetryException {
        Exception exceptionThrown = null;
        for (int count = 0; count < maxAttempts; count++) {
            try {
                runnable.run();
                return;
            } catch (Exception e) {
                LOG.fine(String.format("[%s] Retrying %s%n because of %s %n", count, runnable, e));
                exceptionThrown = e;
                sleep(delay);
            }
        }

        if(exceptionThrown != null) {
            throw new RetryException(exceptionThrown);
        }
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}