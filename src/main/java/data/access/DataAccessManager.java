package data.access;

import speed.test.entities.Test;
import speed.test.web.site.entities.SpeedTestWebSite;

import java.util.List;

/**
 * Created by dango on 6/3/17.
 */
public class DataAccessManager implements DataAccessService {

    @Override
    public void saveTest(Test test) {
        System.out.println("save test " + this.getClass().getName());
    }

    @Override
    public SpeedTestWebSite getNextSpeedTestWebSite() {
        System.out.println("getNextSpeedTestWebSite " + this.getClass().getName());
        return null;
    }

    @Override
    public List<String> getNextUrls(int numOfComparisonPerTest) {
        System.out.println("getNextUrls " + this.getClass().getName());
        return null;
    }
}
