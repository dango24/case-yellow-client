package caseyellow.client.sevices.central;

import caseyellow.client.common.Mapper;
import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.service.SpeedTestWebSiteFactory;
import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.sevices.gateway.model.PreSignedUrl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dango on 6/28/17.
 */
@Component
@Profile("dev")
public class DataAccessServiceStub implements DataAccessService {

    private Logger logger = Logger.getLogger(DataAccessServiceStub.class);

    private int currentWebTest;
    private Mapper mapper;
    private SpeedTestWebSiteFactory speedTestWebSiteFactory;
    private List<String> websiteIdentifiers;

    @Autowired
    public DataAccessServiceStub(Mapper mapper, SpeedTestWebSiteFactory speedTestWebSiteFactory) {
        this.currentWebTest = 0;
        this.mapper = mapper;
        this.websiteIdentifiers = mapper.getWebsiteIdentifiers();
        this.speedTestWebSiteFactory = speedTestWebSiteFactory;
    }

    @Override
    public void sendErrorMessage(String errorMessage) {
        logger.error(errorMessage);
    }

    @Override
    public void saveTest(Test test) {
        System.out.println("save test at stub mode" + test);
    }

    @Override
    public int additionalTimeForWebTestToFinishInSec() {
        return 0;
    }

    @Override
    public SpeedTestMetaData getNextSpeedTestWebSite() {
        currentWebTest++;
        return speedTestWebSiteFactory.getSpeedTestWebSiteFromIdentifier(websiteIdentifiers.get(currentWebTest %websiteIdentifiers.size()));
    }

    @Override
    public List<FileDownloadMetaData> getNextUrls(int numOfComparisonPerTest) {
        List<String> urls = mapper.getUrls();
        Collections.shuffle(urls);

        return urls.subList(0, numOfComparisonPerTest)
                   .stream()
                   .map(url -> new FileDownloadMetaData(mapper.getFileNameFromUrl(url), url))
                   .collect(Collectors.toList());
    }

    @Override
    public PreSignedUrl generatePreSignedUrl(String userIP, String fileName) {
        return new PreSignedUrl(null);
    }
}
