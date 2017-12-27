package caseyellow.client.domain.interfaces;

import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.sevices.gateway.model.PreSignedUrl;

import java.util.List;

/**
 * Created by dango on 6/3/17.
 */
public interface DataAccessService {
    void notifyFailedTest(SpeedTestWebSite failedSpeedTestWebSite, String clientIP);
    void saveTest(Test test) throws RequestFailureException;
    SpeedTestMetaData getNextSpeedTestWebSite();
    List<FileDownloadMetaData> getNextUrls(int numOfComparisonPerTest);
    PreSignedUrl generatePreSignedUrl(String userIP, String fileName);
}
