package caseyellow.client.presistance.service;

import caseyellow.client.common.Mapper;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.website.service.SpeedTestWebSiteFactory;
import caseyellow.client.domain.interfaces.DataAccessService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by dango on 6/28/17.
 */
@Service
@Profile("beta")
public class DataAccessServiceStub implements DataAccessService {

    private Logger logger = Logger.getLogger(DataAccessServiceStub.class);
    private Mapper mapper;
    private SpeedTestWebSiteFactory speedTestWebSiteFactory;

    @Autowired
    public DataAccessServiceStub(Mapper mapper, SpeedTestWebSiteFactory speedTestWebSiteFactory) {
        this.mapper = mapper;
        this.speedTestWebSiteFactory = speedTestWebSiteFactory;
    }

    @Override
    public void saveTest(Test test) {
        logger.debug("save test at stub mode" + test);
    }

    @Override
    public SpeedTestWebSite getNextSpeedTestWebSite() {
        List<String> websiteIdentifiers = mapper.getWebsiteIdentifiers();
        int index = new Random().nextInt(websiteIdentifiers.size());

        return speedTestWebSiteFactory.createSpeedTestWebSiteFromIdentifier(websiteIdentifiers.get(index));
    }

    @Override
    public List<String> getNextUrls(int numOfComparisonPerTest) {
        List<String> urls = mapper.getUrls();
        Collections.shuffle(urls);

        return urls.subList(0, numOfComparisonPerTest);
    }
}
