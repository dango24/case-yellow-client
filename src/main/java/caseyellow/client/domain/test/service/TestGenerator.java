package caseyellow.client.domain.test.service;

import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.file.service.DownloadFileService;
import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.domain.interfaces.ResponsiveService;
import caseyellow.client.domain.interfaces.SystemService;
import caseyellow.client.domain.test.commands.StartProducingTestsCommand;
import caseyellow.client.domain.test.commands.StopProducingTestsCommand;
import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.website.service.WebSiteService;
import caseyellow.client.exceptions.ConnectionException;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.exceptions.UserInterruptException;
import caseyellow.client.exceptions.WebSiteDownloadInfoException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static caseyellow.client.common.Utils.generateUniqueID;
import static java.util.stream.Collectors.toList;

/**
 * Created by dango on 6/3/17.
 */
@Component("testGenerator")
public class TestGenerator implements TestService, StartProducingTestsCommand, StopProducingTestsCommand {

    private Logger logger = Logger.getLogger(TestGenerator.class);

    @Value("${numOfComparisonPerTest}")
    private int numOfComparisonPerTest;

    private AtomicBoolean toProduceTests;
    private SystemService systemService;
    private WebSiteService webSiteService;
    private DataAccessService dataAccessService;
    private DownloadFileService downloadFileService;
    private ExecutorService executorService;
    private MessagesService messagesService;
    private ResponsiveService responsiveService;

    public TestGenerator() {
        this.toProduceTests = new AtomicBoolean(false);
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void start() {
        try {
            produceTests();

        } catch (Exception e) {
            logger.error("Produce tests failed, currently stop creating nre test until user interaction" + e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        toProduceTests.set(false);
    }

    private void produceTests() throws InterruptedException {
        Test test;
        Thread.currentThread().setName("TestProducer-Thread");

        while (toProduceTests.get()) {

            try {
                test = generateNewTest();
                saveTest(test);

            } catch (ConnectionException e) {
                handleError("Connection with host failed, " + e.getMessage(), e);
                handleLostConnection();

            } catch (UserInterruptException e) {
                handleError("Stop to produce test, user interrupt action" + e.getMessage(), e);

            } catch (Exception e) {
                handleError("Failed to produce test, " + e.getMessage(), e);
            }
        }
    }

    private Test generateNewTest() throws UserInterruptException, FileDownloadInfoException {

        SystemInfo systemInfo = systemService.getSystemInfo();
        SpeedTestMetaData speedTestWebSite = dataAccessService.getNextSpeedTestWebSite();
        List<FileDownloadMetaData> filesDownloadMetaData = dataAccessService.getNextUrls(numOfComparisonPerTest);

        List<ComparisonInfo> comparisonInfoList =
                filesDownloadMetaData.stream()
                                     .map(fileDownloadMetaData -> generateComparisonInfo(speedTestWebSite, fileDownloadMetaData))
                                     .peek(comparisonInfo -> notifyFailedTest(comparisonInfo, systemInfo.getPublicIP()))
                                     .filter(ComparisonInfo::isSuccess)
                                     .collect(toList());

        messagesService.testDone();

        return new Test.TestBuilder(generateUniqueID())
                       .addSpeedTestWebsite(speedTestWebSite.getIdentifier())
                       .addComparisonInfoTests(comparisonInfoList)
                       .addSystemInfo(systemInfo)
                       .build();
    }

    private ComparisonInfo generateComparisonInfo(SpeedTestMetaData speedTestMetaData, FileDownloadMetaData fileDownloadMetaData) throws FileDownloadInfoException, WebSiteDownloadInfoException, UserInterruptException, ConnectionException {
        FileDownloadInfo fileDownloadInfo;
        messagesService.subTestStart();
        SpeedTestWebSite speedTestWebSiteDownloadInfo = webSiteService.produceSpeedTestWebSite(speedTestMetaData);

        if (speedTestWebSiteDownloadInfo.isSucceed()) {
            fileDownloadInfo = downloadFileService.generateFileDownloadInfo(fileDownloadMetaData);
        } else {
            fileDownloadInfo = FileDownloadInfo.emptyFileDownloadInfo();
        }

        return new ComparisonInfo(speedTestWebSiteDownloadInfo, fileDownloadInfo);
    }

    private void saveTest(Test test) {
        CompletableFuture.supplyAsync(() -> test)
                         .exceptionally(this::saveTestExceptionHandler)
                         .thenAccept(dataAccessService::saveTest);
    }

    private void notifyFailedTest(ComparisonInfo comparisonInfo, String clientIP) {
        if (comparisonInfo.failed()) {
            logger.info("Receive failed test: " + comparisonInfo.getSpeedTestWebSite());
            dataAccessService.notifyFailedTest(comparisonInfo.getSpeedTestWebSite(), clientIP);
        }
    }

    private Test saveTestExceptionHandler(Throwable e) {
        logger.error("Failed to save urls, " + e.getMessage(), e);
        return null;
    }

    private void handleError(String errorMessage, Exception e) {
        logger.error(errorMessage, e);
    }

    private void handleLostConnection() throws InterruptedException {
        logger.info("Wait for 20 seconds before new attempt to produce new test");
        TimeUnit.SECONDS.sleep(20);
    }

    @Override
    public void executeStartProducingTestsCommand() {
        toProduceTests.set(true);
        executorService.submit(this::start);
        responsiveService.keepAlive();
    }

    @Override
    public void executeStopProducingCommand() {
        try {
            stop();
            webSiteService.close();
            downloadFileService.close();
            responsiveService.close();

        } catch (Exception e) {
            handleError("Error occurred while user cancel request, " + e.getMessage(), e);
        }
    }

    // Setters

    @Autowired
    public void setWebSiteService(WebSiteService webSiteService) {
        this.webSiteService = webSiteService;
    }

    @Autowired
    public void setDataAccessService(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @Autowired
    public void setDownloadFileService(DownloadFileService downloadFileService) {
        this.downloadFileService = downloadFileService;
    }

    @Autowired
    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    @Autowired
    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Autowired
    public void setResponsiveService(ResponsiveService responsiveService) {
        this.responsiveService = responsiveService;
    }
}
