package caseyellow.client.domain.interfaces;

import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.RequestFailureException;

import java.util.List;

/**
 * Created by dango on 6/3/17.
 */
public interface DataAccessService {
    void sendErrorMessage(String errorMessage);
    void saveTest(Test test) throws RequestFailureException;
    int additionalTimeForWebTestToFinishInSec();
    SpeedTestWebSite getNextSpeedTestWebSite();
    List<String> getNextUrls(int numOfComparisonPerTest);
}
