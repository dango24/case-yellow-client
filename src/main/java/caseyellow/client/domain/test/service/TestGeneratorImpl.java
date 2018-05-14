package caseyellow.client.domain.test.service;

import caseyellow.client.common.FileUtils;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.sevices.gateway.services.DataAccessService;
import caseyellow.client.domain.system.ResponsiveService;
import caseyellow.client.domain.test.commands.StartProducingTestsCommand;
import caseyellow.client.domain.test.commands.StopProducingTestsCommand;
import caseyellow.client.exceptions.*;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.presentation.MainFrame;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static caseyellow.client.common.FileUtils.cleanRootDir;

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

    @PostConstruct
    private void init() {
        logger.info(String.format("Run client version %s", clientVersion));
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void start() {
        try {
            Thread.currentThread().setName("Test-Producer");
            produceTests();

        } catch (WebDriverException e) {
            logger.warn(String.format("Web driver error accrued %s", e.getMessage()), e);
            stopProducingTests();
        } catch (Exception e) {
            logger.error(String.format("Produce tests failed: %s", e.getMessage()), e);
            sleep(30);
        }
    }

    private void produceTests() {
        Test test;
        String correlationId;

        while (toProduceTests.get()) {

            try {
                correlationId = String.format("%s-%s", dataAccessService.getUser(), clientVersion);
                MDC.put("correlation-id", correlationId);

                long startTest = System.currentTimeMillis();
                test = testService.generateNewTest();
                long endTest = System.currentTimeMillis();

                test.setStartTime(startTest);
                test.setEndTime(endTest);

                dataAccessService.saveTest(test);

            } catch (ConnectionException e) {
                logger.error("Connection with host failed, " + e.getMessage(), e);
                handleLostConnection();

            } catch (UserInterruptException e) {
                logger.error("Stop to produce test, user interrupt action" + e.getMessage(), e);

            } catch (RequestFailureException e) {
                logger.error("Request failed with status code: " + e.getErrorCode() + ", error message: " + e.getMessage());
                handleRequestFailure(e.getErrorCode());

            } catch (TestException e) {
                logger.error(String.format("Failed to generate test, cause: %s", e.getMessage()), e);

            } finally {
                cleanRootDir();
                MDC.remove("correlation-id");
            }
        }
    }

    private void handleRequestFailure(int errorCode) {
        switch (errorCode) {
            case TOKEN_EXPIRED_CODE:
                logger.warn("Token expired disable app");
                mainFrame.disableApp(true);
                break;

            default:
                handleLostConnection();
                break;
        }
    }

    private void handleLostConnection() {
        String errorMessage = "Lost connection, wait for 35 seconds before new attempt to produce new test";
        logger.warn(errorMessage);
        try {
            testService.stop();
        } catch (WebDriverException | IOException e) {
            logger.warn(String.format("Stop web driver while lost connection, ", e.getMessage()), e);
        }
        sleep(35, errorMessage);
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

    private void sleep(long timeoutInSec) {
        sleep(timeoutInSec, String.format("Sleep for %s seconds", timeoutInSec));
    }

    private void sleep(long timeoutInSec, String message) {
        try {
            mainFrame.showMessage(message);
            TimeUnit.SECONDS.sleep(timeoutInSec);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
