package caseyellow.client.domain.website.service;

import caseyellow.client.exceptions.WebSiteDownloadInfoException;
import caseyellow.client.domain.website.model.SpeedTestWebSiteDownloadInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.FindFailedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class WebSiteServiceImpl implements WebSiteService {

    public static final int DELAY_TIME_BEFORE_SNAPSHOT = 1000;
    // Logger
    private Logger logger = Logger.getLogger(WebSiteServiceImpl.class);

    // Fields
    private BrowserService browserService;

    // Setters

    @Autowired
    public void setBrowserService(BrowserService browserService) {
        this.browserService = browserService;
    }

    // Methods

    @Override
    public SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite) throws WebSiteDownloadInfoException {
        String websiteSnapshot;
        long startMeasuringTimestamp;

        try {
            browserService.openBrowser(speedTestWebsite.webSiteUrl());
            browserService.centralizedWebPage(speedTestWebsite.getIdentifier());

            if (speedTestWebsite.isFlashable()) {
                browserService.pressStartTestButton(speedTestWebsite.getIdentifier());
            }

            logger.info("Start '" + speedTestWebsite.getIdentifier() + "' speed test");
            startMeasuringTimestamp = System.currentTimeMillis();
            browserService.waitForTestToFinish(speedTestWebsite.getIdentifier());

            TimeUnit.MILLISECONDS.sleep(DELAY_TIME_BEFORE_SNAPSHOT);
            websiteSnapshot = browserService.takeScreenSnapshot();

            return new SpeedTestWebSiteDownloadInfo.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                                   .setSucceed()
                                                   .setStartDownloadingTimeSnapshot(startMeasuringTimestamp)
                                                   .setWebSiteDownloadInfoSnapshot(websiteSnapshot)
                                                   .build();

        } catch (FindFailedException | InterruptedException e) {
            logger.error("Failed to complete speed test " + speedTestWebsite.getIdentifier() + ", " + e.getMessage(), e);
            return new SpeedTestWebSiteDownloadInfo.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                                   .setFailure()
                                                   .setWebSiteDownloadInfoSnapshot(browserService.takeScreenSnapshot())
                                                   .build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WebSiteDownloadInfoException(e.getMessage());
        } finally {
            browserService.closeBrowser();
        }
    }

    @Override
    public void close() throws IOException {
        browserService.closeBrowser();
    }
}
