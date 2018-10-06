package caseyellow.client.domain.test.service;

import caseyellow.client.common.FileUtils;
import caseyellow.client.domain.logger.services.CYLogger;
import caseyellow.client.domain.system.SystemService;
import caseyellow.client.sevices.gateway.services.DataAccessService;
import caseyellow.client.domain.system.ResponsiveService;
import caseyellow.client.exceptions.*;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.presentation.MainFrame;
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
public class TestGeneratorImpl implements TestGenerator {

    private CYLogger logger = new CYLogger(TestGeneratorImpl.class);

    public static final int TOKEN_EXPIRED_CODE = 701;

    @Value("${client.version}")
    private String clientVersion;

    private MainFrame mainFrame;
    private Future<?> testTask;
    private AtomicBoolean toProduceTests;
    private TestService testService;
    private SystemService systemService;
    private DataAccessService dataAccessService;
    private ExecutorService executorTestService;
    private ResponsiveService responsiveService;

    @Autowired
    public TestGeneratorImpl(TestService testService, DataAccessService dataAccessService, ResponsiveService responsiveService, SystemService systemService) {
        this.testService = testService;
        this.dataAccessService = dataAccessService;
        this.responsiveService = responsiveService;
        this.systemService = systemService;
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
        }
    }

    private void produceTests() {
        Test test;

        while (toProduceTests.get()) {

            try {
                int correlationId = dataAccessService.getTestLifeCycle();
                MDC.put("correlation-id", correlationId);
                reportSystemPerformance();

                long startTest = System.currentTimeMillis();
                test = testService.generateNewTest();
                long endTest = System.currentTimeMillis();

                test.setStartTime(startTest);
                test.setEndTime(endTest);
                test.setComputerIdentifier(FileUtils.getComputerIdentifier());

                mainFrame.showMessage("Save test");
                dataAccessService.saveTest(test);

            } catch (ConnectionException e) {
                logger.error("Connection with host failed, " + e.getMessage(), e);
                handleLostConnection();

            } catch (RequestFailureException e) {
                logger.error("Request failed with status code: " + e.getErrorCode() + ", error message: " + e.getMessage());
                handleRequestFailure(e.getErrorCode());

            } catch (TestException | UserInterruptException e) {
                logger.error(String.format("Failed to generate test, cause: %s", e.getMessage()), e);

            } catch (WebDriverException e) {
                throw e; // Throw WebDriverException again for handling it in start method

            } catch (Exception e) {
                logger.error(String.format("Produce tests failed: %s", e.getMessage()), e);
                sleep(20);

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
        int sleepTimeInSec = 20;
        String errorMessage = String.format("Lost connection, wait for %s seconds before new attempt to produce new test", sleepTimeInSec);
        logger.warn(errorMessage);
        try {
            testService.stop();
        } catch (WebDriverException | IOException e) {
            logger.warn(String.format("Stop web driver while lost connection, ", e.getMessage()), e);
        }
        sleep(sleepTimeInSec, errorMessage);
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

    @Override
    public void updateTestLifeCycle() {
        dataAccessService.updateTestLifeCycle();
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

    private void reportSystemPerformance() throws IOException {
        double memoryUsage = systemService.getJvmUsedMemory();
        double memoryTotal = systemService.getJvmMaxMemory();
        double memoryFree = memoryTotal - memoryUsage;
        logger.info("jvm cpu usage (percent): " + systemService.getJvmCpuLoad());
        logger.info("jvm total memory (Megabytes): " + memoryTotal);
        logger.info("jvm free memory (Megabytes): " + memoryFree);
        logger.info("jvm memory usage (Megabytes): " + memoryUsage);
    }
}
