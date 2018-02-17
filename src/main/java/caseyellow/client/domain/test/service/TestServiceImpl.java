package caseyellow.client.domain.test.service;

import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.file.service.DownloadFileService;
import caseyellow.client.domain.data.access.DataAccessService;
import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.system.SystemService;
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
        List<FileDownloadProperties> fileDownloadProperties = dataAccessService.getNextUrls();
        logger.info(String.format("Start producing test with speed-test: %s, urls: %s", speedTestWebSite.getIdentifier(), fileDownloadProperties.stream().map(FileDownloadProperties::getIdentifier).collect(joining(", "))));

        List<ComparisonInfo> comparisonInfoList =
                fileDownloadProperties.stream()
                                      .map(fileDownloadMetaData -> generateComparisonInfo(speedTestWebSite, fileDownloadMetaData))
                                      .peek(comparisonInfo -> checkFailedTest(comparisonInfo, systemInfo.getPublicIP()))
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
    }

    private ComparisonInfo generateComparisonInfo(SpeedTestMetaData speedTestMetaData, FileDownloadProperties fileDownloadProperties) throws FileDownloadInfoException, WebSiteDownloadInfoException, UserInterruptException, ConnectionException {
        FileDownloadInfo fileDownloadInfo;
        messagesService.subTestStart();
        SpeedTestWebSite speedTestWebSiteDownloadInfo = webSiteService.produceSpeedTestWebSite(speedTestMetaData);

        if (speedTestWebSiteDownloadInfo.isSucceed()) {
            fileDownloadInfo = downloadFileService.generateFileDownloadInfo(fileDownloadProperties);
        } else {
            fileDownloadInfo = FileDownloadInfo.emptyFileDownloadInfo();
        }

        return new ComparisonInfo(speedTestWebSiteDownloadInfo, fileDownloadInfo);
    }

    private void checkFailedTest(ComparisonInfo comparisonInfo, String clientIP) {
        if (comparisonInfo.failed()) {
            messagesService.testDone();
            dataAccessService.notifyFailedTest(comparisonInfo, clientIP);

            throw new TestException("Failed to generate test");
        }
    }
}
