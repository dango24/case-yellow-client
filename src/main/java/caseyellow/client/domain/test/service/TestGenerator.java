package caseyellow.client.domain.test.service;

import caseyellow.client.common.Validator;
import caseyellow.client.domain.file.service.DownloadFileService;
import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.interfaces.SystemService;
import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSiteDownloadInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.website.service.WebSiteService;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.test.model.Test;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static caseyellow.client.common.Utils.generateUniqueID;
import static java.util.stream.Collectors.toList;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class TestGenerator implements TestService {

    // Logger
    private Logger logger = Logger.getLogger(TestGenerator.class);

    // Constants
    @Value("${numOfComparisonPerTest}")
    private int numOfComparisonPerTest;

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

        while (toProduceTests.get()) {

            try {
                test = generateNewTest();

                if (Validator.validateTest(test)) {
                    saveTest(test);
                }

            } catch (Exception e) {
                logger.error("Failed to produce test, " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void stop() {
        toProduceTests.set(false);
    }

    private Test generateNewTest() {
        Test test;
        SystemInfo systemInfo;
        SpeedTestWebSite speedTestWebSite;
        List<String> urls;
        List<ComparisonInfo> comparisonInfoList;

        systemInfo = systemService.getSystemInfo();
        speedTestWebSite = dataAccessService.getNextSpeedTestWebSite();
        urls = dataAccessService.getNextUrls(numOfComparisonPerTest);

        comparisonInfoList = urls.stream()
                                 .map(url -> generateComparisonInfo(speedTestWebSite, url))
                                 .collect(toList());

        test = new Test.TestBuilder(generateUniqueID())
                       .addSpeedTestWebsite(speedTestWebSite.getIdentifier())
                       .addComparisonInfoTests(comparisonInfoList)
                       .addSystemInfo(systemInfo)
                       .build();

        return test;
    }

    private ComparisonInfo generateComparisonInfo(SpeedTestWebSite speedTestWebSite, String url) throws FileDownloadInfoException {
        SpeedTestWebSiteDownloadInfo speedTestWebSiteDownloadInfo = webSiteService.produceSpeedTestWebSiteDownloadInfo(speedTestWebSite);
        FileDownloadInfo fileDownloadInfo = downloadFileService.generateFileDownloadInfo(url);

        return new ComparisonInfo(speedTestWebSiteDownloadInfo, fileDownloadInfo);
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
}
