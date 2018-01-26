package caseyellow.client.domain.test.service;

import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.file.service.DownloadFileService;
import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.domain.interfaces.SystemService;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.website.service.WebSiteService;
import caseyellow.client.exceptions.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static caseyellow.client.common.Utils.generateUniqueID;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Service
public class TestServiceImpl implements TestService {

    private Logger logger = Logger.getLogger(TestServiceImpl.class);

    @Value("${numOfComparisonPerTest}")
    private int numOfComparisonPerTest;

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

        SystemInfo systemInfo = systemService.getSystemInfo();
        SpeedTestMetaData speedTestWebSite = dataAccessService.getNextSpeedTestWebSite();
        List<FileDownloadMetaData> filesDownloadMetaData = dataAccessService.getNextUrls(numOfComparisonPerTest);
        logger.info(String.format("Start producing test with speedtest: %s, urls: %s", speedTestWebSite.getIdentifier(), filesDownloadMetaData.stream().map(FileDownloadMetaData::getFileName).collect(joining(", "))));

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

    @Override
    public void stop() throws IOException {
        webSiteService.close();
        downloadFileService.close();
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

    private void notifyFailedTest(ComparisonInfo comparisonInfo, String clientIP) {
        if (comparisonInfo.failed()) {
            messagesService.testDone();
            dataAccessService.notifyFailedTest(comparisonInfo, clientIP);
        }
    }
}
