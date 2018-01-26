package caseyellow.client.domain.test.service;

import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.interfaces.ResponsiveService;
import caseyellow.client.domain.test.commands.StartProducingTestsCommand;
import caseyellow.client.domain.test.commands.StopProducingTestsCommand;
import caseyellow.client.exceptions.*;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.presentation.MainFrame;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dango on 6/3/17.
 */
@Component("testGenerator")
public class TestGeneratorImpl implements TestGenerator, StartProducingTestsCommand, StopProducingTestsCommand {

    private Logger logger = Logger.getLogger(TestGeneratorImpl.class);

    public static final int TOKEN_EXPIRED_CODE = 701;

    private MainFrame mainFrame;
    private AtomicBoolean toProduceTests;
    private TestService testService;
    private DataAccessService dataAccessService;
    private ExecutorService executorService;
    private ResponsiveService responsiveService;

    @Autowired
    public TestGeneratorImpl(TestService testService, DataAccessService dataAccessService, ResponsiveService responsiveService) {
        this.testService = testService;
        this.dataAccessService = dataAccessService;
        this.responsiveService = responsiveService;
        this.toProduceTests = new AtomicBoolean(false);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void start() {
        try {
            produceTests();

        } catch (Exception e) {
            logger.error("Produce tests failed" + e.getMessage(), e);
        }
    }

    private void produceTests() throws InterruptedException {
        Test test;
        Thread.currentThread().setName("TestProducer-Thread");

        while (toProduceTests.get()) {

            try {
                test = testService.generateNewTest();
                saveTest(test);

            } catch (ConnectionException e) {
                logger.error("Connection with host failed, " + e.getMessage(), e);
                handleLostConnection();

            } catch (UserInterruptException e) {
                logger.error("Stop to produce test, user interrupt action" + e.getMessage(), e);

            } catch (RequestFailureException e) {
                logger.error("Request failed with status code: " + e.getErrorCode() + ", error message: " + e.getMessage());
                handleRequestFailure(e.getErrorCode());
            }
        }
    }

    private void handleRequestFailure(int errorCode) {
        switch (errorCode) {
            case TOKEN_EXPIRED_CODE:
                mainFrame.disableApp(true);
                break;
        }
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

    private void handleLostConnection() throws InterruptedException {
        stopProducingTests();
        logger.info("Wait for 30 seconds before new attempt to produce new test");
        TimeUnit.SECONDS.sleep(30);
        startProducingTests();
    }

    @Override
    public void startProducingTests() {
        toProduceTests.set(true);
        executorService.submit(this::start);
        responsiveService.keepAlive();
    }

    @Override
    public void stopProducingTests() {
        try {
            toProduceTests.set(false);
            responsiveService.close();

        } catch (Exception e) {
            logger.error("Error occurred while user cancel request, " + e.getMessage(), e);
        }
    }
}
