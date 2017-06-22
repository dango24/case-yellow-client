package app.caseyellow.client.domain.services.interfaces;

import app.caseyellow.client.domain.model.test_entites.Test;
import app.caseyellow.client.domain.model.web_site_entites.SpeedTestWebSite;

import java.util.List;

/**
 * Created by dango on 6/3/17.
 */
public interface DataAccessService {
    void saveTest(Test test);
    SpeedTestWebSite getNextSpeedTestWebSite();
    List<String> getNextUrls(int numOfComparisonPerTest);
}
