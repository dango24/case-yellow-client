package caseyellow.client.sevices.gateway.services;

import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.logger.model.LogData;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.sevices.gateway.model.PreSignedUrl;

import java.util.List;

/**
 * Created by dango on 6/3/17.
 */
public interface DataAccessService {
    void notifyFailedTest(ComparisonInfo comparisonInfo, String clientIP);
    void saveTest(Test test) throws RequestFailureException;
    void startTest(String identifier, List<String> urls);
    SpeedTestMetaData getNextSpeedTestWebSite();
    List<FileDownloadProperties> getNextUrls();
    PreSignedUrl generatePreSignedUrl(String fileKey);
    String getUser();
    String clientVersion();
    List<String> getChromeOptionsArguments();
    void uploadFileToServer(String key, String fileToUpload);
    void uploadLogData(LogData logData);
    int getTestLifeCycle();
    void updateTestLifeCycle();
    boolean runClassicTest();
}
