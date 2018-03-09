package caseyellow.client.domain.test.service;

import caseyellow.client.domain.data.access.DataAccessService;
import caseyellow.client.domain.system.ResponsiveService;
import caseyellow.client.domain.test.commands.StartProducingTestsCommand;
import caseyellow.client.domain.test.commands.StopProducingTestsCommand;
import caseyellow.client.exceptions.*;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.presentation.MainFrame;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${client.version}")
    private String clientVersion;

    private MainFrame mainFrame;
    private Future<?> testTask;
    private AtomicBoolean toProduceTests;
    private TestService testService;
    private DataAccessService dataAccessService;
    private ExecutorService executorTestService;
    private ResponsiveService responsiveService;

    @Autowired
    public TestGeneratorImpl(TestService testService, DataAccessService dataAccessService, ResponsiveService responsiveService) {
        this.testService = testService;
        this.dataAccessService = dataAccessService;
        this.responsiveService = responsiveService;
        this.toProduceTests = new AtomicBoolean(false);
        this.executorTestService = Executors.newSingleThreadExecutor();
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void start() {
        try {
            Thread.currentThread().setName("Test-Producer");
            produceTests();

        } catch (Exception e) {
            logger.error("Produce tests failed" + e.getMessage(), e);
        }
    }

    private void produceTests() throws InterruptedException {
        Test test;

        while (toProduceTests.get()) {

            try {
                MDC.put("correlation-id", clientVersion);
                test = testService.generateNewTest();
                saveTest(test);

            } catch (ConnectionException e) {
                logger.error("Connection with host failed, " + e.getMessage(), e);
                toProduceTests.set(false);
                CompletableFuture.runAsync(() -> handleLostConnection());

            } catch (UserInterruptException e) {
                logger.error("Stop to produce test, user interrupt action" + e.getMessage(), e);

            } catch (RequestFailureException e) {
                logger.error("Request failed with status code: " + e.getErrorCode() + ", error message: " + e.getMessage());
                handleRequestFailure(e.getErrorCode());

            } catch (TestException e) {
                logger.error(String.format("Failed to generate test, cause: %s", e.getMessage()), e);

            } finally {
                MDC.remove("correlation-id");
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
                         .thenAccept(dataAccessService::saveTest);
    }

    @Override
    public void handleLostConnection()  {
        try {
            stopProducingTests();
            logger.info("Lost connection, wait for 35 seconds before new attempt to produce new test");
            mainFrame.showMessage("Lost connection, wait for 35 seconds before new attempt to produce new test");
            TimeUnit.SECONDS.sleep(35);
            startProducingTests();

        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void startProducingTests() {
        toProduceTests.set(true);
        testTask = executorTestService.submit(this::start);
        responsiveService.keepAlive();
    }

    @Override
    public void stopProducingTests() {
        try {
            toProduceTests.set(false);
            responsiveService.close();
            testService.stop();
            testTask.cancel(true);
            mainFrame.testStopped();

        } catch (Exception e) {
            logger.error("Error occurred while user cancel request, " + e.getMessage(), e);
        }
    }
}
