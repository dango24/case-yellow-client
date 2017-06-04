package app.access;

import app.test.entities.Test;
import app.test.web.site.entities.SpeedTestWebSite;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dango on 6/4/17.
 */
@Service
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
