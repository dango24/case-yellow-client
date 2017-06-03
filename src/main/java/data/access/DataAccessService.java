package data.access;

import speed.test.entities.Test;
import speed.test.web.site.entities.SpeedTestWebSite;

import java.util.List;

/**
 * Created by dango on 6/3/17.
 */
public interface DataAccessService {
    void saveTest(Test test);
    SpeedTestWebSite getNextSpeedTestWebSite();
    List<String> getNextUrls(int numOfComparisonPerTest);
}
