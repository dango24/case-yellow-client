package caseyellow.client.domain.services.interfaces;

import caseyellow.client.domain.model.test.Test;
import caseyellow.client.domain.model.website.SpeedTestWebSite;

import java.util.List;

/**
 * Created by dango on 6/3/17.
 */
public interface DataAccessService {
    void saveTest(Test test);
    SpeedTestWebSite getNextSpeedTestWebSite();
    List<String> getNextUrls(int numOfComparisonPerTest);
}
