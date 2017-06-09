package app.mock.access;

import app.access.DataAccessService;
import app.test.entities.Test;
import app.test.web.site.entities.SpeedTestWebSite;
import app.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

/**
 * Created by dango on 6/6/17.
 */
public class DataAccessManagerMock implements DataAccessService {

    // Logger
    private Logger logger = Logger.getLogger(DataAccessManagerMock.class);

    // Fields
    private Utils utils;
    private String speedTestWebSiteClassName;

    // Constructor
    public DataAccessManagerMock(String speedTestWebSiteClassName) {
        this.speedTestWebSiteClassName = speedTestWebSiteClassName;
    }

    // Methods

    @Autowired
    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    @Override
    public void saveTest(Test test) {
        logger.info("save test at DataAccessManagerMock");
    }

    @Override
    public SpeedTestWebSite getNextSpeedTestWebSite() {
        try {
            System.out.println("getNextSpeedTestWebSite " + this.getClass().getName());
            Class<?> clazz = Class.forName(speedTestWebSiteClassName);
            Constructor<?> constructor = clazz.getConstructor();
            return (SpeedTestWebSite)constructor.newInstance();

        } catch (Exception e) {
            logger.error("Failed to init SpeedTestWebSite from DataAccessManagerMock " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<String> getNextUrls(int numOfComparisonPerTest) {
        List<String> urls = utils.getUrls();
        Collections.shuffle(urls);

        return urls.subList(0, numOfComparisonPerTest);
    }
}
