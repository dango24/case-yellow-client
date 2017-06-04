package app.engine;

import app.access.DataAccessService;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import app.test.entities.*;
import app.test.web.site.services.DownloadFileService;
import app.test.web.site.services.WebSiteService;
import app.test.web.site.entities.SpeedTestWebSite;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static app.utils.SystemUtils.getSystemInfo;
import static app.utils.Validator.validateTest;
import static java.util.stream.Collectors.toList;
import static app.utils.Utils.generateUniqueID;

/**
 * Created by dango on 6/3/17.
 */
@Component
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
        this.toProduceTests = new AtomicBoolean(true);
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


    // Methods

    public void produceTests() {
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
