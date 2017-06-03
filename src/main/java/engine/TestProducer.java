package engine;

import data.access.DataAccessService;
import org.apache.log4j.Logger;
import speed.test.entities.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dango on 6/3/17.
 */
public class TestProducer {

    // Logger
    private Logger logger = Logger.getLogger(TestProducer.class);

    // Fields
    private AtomicBoolean toProduceTests;
    private DataAccessService dataAccessService;

    // Constructor
    public TestProducer() {

    }

    // Methods

    private void produceTests() {
        Test test;

        while (toProduceTests.get()) {
            test = produceNewTest();
            saveTest(test);
        }
    }

    private Test produceNewTest() {
        return null;
    }

    private void saveTest(Test test) {
        CompletableFuture.supplyAsync(() -> test)
                         .exceptionally(this::saveTestExceptionHandler)
                         .thenAccept(dataAccessService::saveTest);
    }

    private Test saveTestExceptionHandler(Throwable e) {
        logger.error("Failed to save test, " + e.getMessage(), e);
        return null;
    }
}
