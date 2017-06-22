package app.caseyellow.client.domain.services.impl;

import app.caseyellow.client.common.Utils;
import app.caseyellow.client.common.Validator;
import app.caseyellow.client.domain.model.SystemInfo;
import app.caseyellow.client.domain.model.test_entites.ComparisonInfo;
import app.caseyellow.client.domain.model.test_entites.SpeedTestWebSiteDownloadInfo;
import app.caseyellow.client.domain.model.web_site_entites.SpeedTestWebSite;
import app.caseyellow.client.domain.services.*;
import app.caseyellow.client.exceptions.FileDownloadInfoException;
import app.caseyellow.client.domain.model.test_entites.FileDownloadInfo;
import app.caseyellow.client.domain.model.test_entites.Test;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.Collectors.toList;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class TestGenerator implements TestService {

    // Logger
    private Logger logger = Logger.getLogger(TestGenerator.class);

    // Constants
    private final int NUM_OF_COMPARISON_PER_TEST = 3;

    // Fields
    private AtomicBoolean toProduceTests;
    private SystemService systemService;
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

    @Autowired
    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    // Methods

    @Override
    public void produceTests() {
        Test test;

        try {

            while (toProduceTests.get()) {
                test = generateNewTest();

                if (Validator.validateTest(test)) {
                    saveTest(test);
                }
            }

        } catch (RuntimeException e) {}
    }

    private Test generateNewTest() {
        Test test;
        SystemInfo systemInfo;
        SpeedTestWebSite speedTestWebSite;
        List<String> urls;
        List<ComparisonInfo> comparisonInfoList;

        systemInfo = systemService.getSystemInfo();
        speedTestWebSite = dataAccessService.getNextSpeedTestWebSite();
        urls = dataAccessService.getNextUrls(NUM_OF_COMPARISON_PER_TEST);

        comparisonInfoList = urls.stream()
                                 .map(url -> generateComparisonInfo(speedTestWebSite, url))
                                 .collect(toList());

        test = new Test.TestBuilder(Utils.generateUniqueID())
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
        logger.error("Failed to save urls, " + e.getMessage(), e);
        return null;
    }

    private ComparisonInfo generateComparisonInfo(SpeedTestWebSite speedTestWebSite, String url) throws FileDownloadInfoException {
        SpeedTestWebSiteDownloadInfo speedTestWebSiteDownloadInfo = webSiteService.produceSpeedTestWebSiteDownloadInfo(speedTestWebSite);
        FileDownloadInfo fileDownloadInfo = downloadFileService.generateFileDownloadInfo(url);

        return new ComparisonInfo(speedTestWebSiteDownloadInfo, fileDownloadInfo);
    }
}
