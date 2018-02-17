package caseyellow.client.domain.data.access;

import caseyellow.client.domain.file.model.FileDownloadProperties;
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
    SpeedTestMetaData getNextSpeedTestWebSite();
    List<FileDownloadProperties> getNextUrls();
    PreSignedUrl generatePreSignedUrl(String userIP, String fileName);
}
