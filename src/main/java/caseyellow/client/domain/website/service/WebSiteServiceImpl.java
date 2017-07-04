package caseyellow.client.domain.website.service;

import caseyellow.client.domain.website.model.SpeedTestWebSiteDownloadInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.FindFailedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class WebSiteServiceImpl implements WebSiteService {

    private Logger logger = Logger.getLogger(WebSiteServiceImpl.class);

    private BrowserService browserService;

    @Autowired
    public WebSiteServiceImpl(BrowserService browserService) {
        this.browserService = browserService;
    }

    @Override
    public SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite) {
        String websiteSnapshot;
        long startMeasuringTimestamp;

        try {
            browserService.openBrowser(speedTestWebsite.webSiteUrl());
            TimeUnit.SECONDS.sleep(2);

            // startTestButton as null indicates no start test button at the specific speed test url
            if (speedTestWebsite.startTestButton() != null) {
                browserService.pressTestButton(speedTestWebsite.startTestButton());
            }

            logger.debug("Start " + speedTestWebsite.getIdentifier() + " speed test");
            startMeasuringTimestamp = System.currentTimeMillis();
            browserService.waitForTestToFinish(speedTestWebsite.testFinishIdentifier(),
                                               speedTestWebsite.waitForTestToFinishInSec());

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
            return null;
        } finally {
            browserService.closeBrowser();
        }
    }
}
