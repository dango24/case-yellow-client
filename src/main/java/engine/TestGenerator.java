package engine;

import data.access.DataAccessService;
import org.apache.log4j.Logger;

import speed.test.entities.*;
import speed.test.web.site.services.DownloadFileService;
import speed.test.web.site.services.WebSiteService;
import speed.test.web.site.entities.SpeedTestWebSite;
import utils.SystemUtils;
import utils.Utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.Collectors.toList;
import static utils.SystemUtils.getSystemInfo;
import static utils.Utils.generateUniqueID;
import static utils.Validator.validateTest;

/**
 * Created by dango on 6/3/17.
 */
public class TestGenerator {

    // Logger
    private Logger logger = Logger.getLogger(TestGenerator.class);

    // Constants
    private final int NUM_OF_COMPARISON_PER_TEST = 3;

    // Fields
    private AtomicBoolean toProduceTests;
    private WebSiteService webSiteService;
    private DataAccessService dataAccessService;
    private DownloadFileService downloadFileService;

    // Constructor
    public TestGenerator() {

    }

    // Methods

    private void produceTests() {
        Test test;

        while (toProduceTests.get()) {
            test = generateNewTest();

            if (validateTest(test)) {
                saveTest(test);
            }
        }
    }

    private Test generateNewTest() {
        Test test;
        SpeedTestWebSite speedTestWebSite;
        SystemInfo systemInfo;
        List<String> urls;
        List<ComparisonInfo> comparisonInfoList;

        systemInfo = getSystemInfo();
        speedTestWebSite = dataAccessService.getNextSpeedTestWebSite();
        urls = dataAccessService.getNextUrls(NUM_OF_COMPARISON_PER_TEST);

        comparisonInfoList = urls.stream()
                                 .map(url -> generateComparisonInfo(speedTestWebSite, url))
                                 .collect(toList());

        test = new Test.TestBuilder(generateUniqueID())
                       .addSpeedTestWebsite(speedTestWebSite)
                       .addComparisonInfoTests(comparisonInfoList)
                       .addSystemInfo(systemInfo)
                       .build();

        return test;
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

    private ComparisonInfo generateComparisonInfo(SpeedTestWebSite speedTestWebSite, String url) {
        SpeedTestWebSiteDownloadInfo speedTestWebSiteDownloadInfo = webSiteService.produceSpeedTestWebSiteDownloadInfo(speedTestWebSite);
        FileDownloadInfo fileDownloadInfo = downloadFileService.generateFileDownloadInfo(url);

        return new ComparisonInfo(speedTestWebSiteDownloadInfo, fileDownloadInfo);
    }
}
