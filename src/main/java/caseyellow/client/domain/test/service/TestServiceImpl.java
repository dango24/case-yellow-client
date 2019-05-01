package caseyellow.client.domain.test.service;

import caseyellow.client.common.FileUtils;
import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.file.service.DownloadFileService;
import caseyellow.client.domain.logger.services.CYLogger;
import caseyellow.client.sevices.gateway.services.DataAccessService;
import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.system.SystemService;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.website.service.WebSiteService;
import caseyellow.client.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static caseyellow.client.common.Utils.generateUniqueID;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Service
public class TestServiceImpl implements TestService {

    private static CYLogger logger = new CYLogger(TestServiceImpl.class);

    private SystemService systemService;
    private WebSiteService webSiteService;
    private MessagesService messagesService;
    private DataAccessService dataAccessService;
    private DownloadFileService downloadFileService;

    @Autowired
    public TestServiceImpl(SystemService systemService, WebSiteService webSiteService, MessagesService messagesService, DataAccessService dataAccessService, DownloadFileService downloadFileService) {
        this.systemService = systemService;
        this.webSiteService = webSiteService;
        this.messagesService = messagesService;
        this.dataAccessService = dataAccessService;
        this.downloadFileService = downloadFileService;
    }

    @Override
    public Test generateNewTest() throws UserInterruptException, FileDownloadInfoException, RequestFailureException {
        String isp;
        SystemInfo systemInfo;
        boolean runClassicTest;
        SpeedTestMetaData speedTestWebSite;
        List<ComparisonInfo> comparisonInfoList;
        List<FileDownloadProperties> fileDownloadProperties;

        isp = systemService.getISP();
        systemInfo = systemService.getSystemInfo();
        logger.info(String.format("System info is: %s. ISP is: %s", systemInfo,isp));

        runClassicTest = dataAccessService.runClassicTest();

        if (runClassicTest) {
            messagesService.showMessage("Run Classic Test");
        }

        messagesService.showMessage("Fetch speed test webSite");
        speedTestWebSite = dataAccessService.getNextSpeedTestWebSite();

        messagesService.showMessage("Fetch file download properties");
        fileDownloadProperties = dataAccessService.getNextUrls();

        dataAccessService.startTest(speedTestWebSite.getIdentifier(), fileDownloadProperties.stream().map(FileDownloadProperties::getIdentifier).collect(toList()));
        logger.info(String.format("Start producing test with speed-test: %s, urls: %s", speedTestWebSite.getIdentifier(), fileDownloadProperties.stream().map(FileDownloadProperties::getIdentifier).collect(joining(", "))));

        comparisonInfoList =
                fileDownloadProperties.stream()
                                      .map(fileDownloadMetaData -> generateComparisonInfo(speedTestWebSite, fileDownloadMetaData, runClassicTest))
                                      .peek(comparisonInfo -> checkFailedTest(comparisonInfo, systemInfo.getPublicIP()))
                                      .collect(toList());

        messagesService.testDone();

        return new Test.TestBuilder(generateUniqueID())
                       .addSpeedTestWebsite(speedTestWebSite.getIdentifier())
                       .addComparisonInfoTests(comparisonInfoList)
                       .addSystemInfo(systemInfo)
                       .addISP(isp)
                       .addClassicTest(runClassicTest)
                       .build();
    }

    @Override
    public void stop() throws IOException {
        webSiteService.close();
    }

    private ComparisonInfo generateComparisonInfo(SpeedTestMetaData speedTestMetaData, FileDownloadProperties fileDownloadProperties, boolean runClassicTest) throws FileDownloadInfoException, WebSiteDownloadInfoException, UserInterruptException, ConnectionException {

        if (runClassicTest) {
            return generateComparisonInfo(speedTestMetaData, fileDownloadProperties);
        } else {
            return generateFileDownloadInfoComparisonInfo(fileDownloadProperties);
        }
    }

    private ComparisonInfo generateComparisonInfo(SpeedTestMetaData speedTestMetaData, FileDownloadProperties fileDownloadProperties) throws FileDownloadInfoException, WebSiteDownloadInfoException, UserInterruptException, ConnectionException {
        FileDownloadInfo fileDownloadInfo;
        messagesService.subTestStart();
        SpeedTestWebSite speedTestWebSiteDownloadInfo = webSiteService.produceSpeedTestWebSite(speedTestMetaData);

        if (speedTestWebSiteDownloadInfo.isSucceed()) {
            fileDownloadInfo = downloadFileService.generateFileDownloadInfo(fileDownloadProperties , false);
        } else {
            fileDownloadInfo = FileDownloadInfo.emptyFileDownloadInfo();
        }

        return new ComparisonInfo(speedTestWebSiteDownloadInfo, fileDownloadInfo);
    }

    private ComparisonInfo generateFileDownloadInfoComparisonInfo(FileDownloadProperties fileDownloadProperties) {
        FileDownloadInfo fileDownloadInfo = downloadFileService.generateFileDownloadInfo(fileDownloadProperties , true);

        return ComparisonInfo.buildComparisonInfoFileDownloadInfo(fileDownloadInfo);
    }

    private void checkFailedTest(ComparisonInfo comparisonInfo, String clientIP) {
        if (comparisonInfo.failed()) {
            messagesService.testDone();
            dataAccessService.notifyFailedTest(comparisonInfo, clientIP);
            FileUtils.deleteFile(comparisonInfo.getSpeedTestWebSite().getWebSiteDownloadInfoSnapshot());

            throw new TestException("Failed to generate test");
        }
    }

    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }
}
