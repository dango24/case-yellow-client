package caseyellow.client.presistance;

import caseyellow.client.domain.model.test.Test;
import caseyellow.client.domain.model.website.SpeedTestWebSite;
import caseyellow.client.domain.services.interfaces.DataAccessService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by dango on 6/4/17.
 */
public class DataAccessServiceImpl implements DataAccessService {

    private Logger logger = Logger.getLogger(DataAccessServiceImpl.class);

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
