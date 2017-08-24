package caseyellow.client.domain.website.service;

import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.exceptions.ConnectionException;
import caseyellow.client.exceptions.UserInterruptException;
import caseyellow.client.exceptions.WebSiteDownloadInfoException;
import caseyellow.client.domain.website.model.SpeedTestWebSiteDownloadInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.browser.BrowserService;
import caseyellow.client.exceptions.BrowserFailedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static caseyellow.client.common.Utils.moveMouseToStartingPoint;
import static caseyellow.client.common.Utils.takeScreenSnapshot;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class WebSiteServiceImpl implements WebSiteService, Closeable {

    public static final int DELAY_TIME_BEFORE_SNAPSHOT = 1000;

    private Logger logger = Logger.getLogger(WebSiteServiceImpl.class);

    private BrowserService browserService;
    private MessagesService messagesService;
    private DataAccessService dataAccessService;

    @Autowired
    public void setDataAccessService(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @Autowired
    public void setBrowserService(BrowserService browserService) {
        this.browserService = browserService;
    }

    @Autowired
    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite) throws UserInterruptException, ConnectionException {
        String websiteSnapshot;
        long startMeasuringTimestamp;

        try {
            messagesService.showMessage("Testing web site: " + speedTestWebsite.webSiteUrl());
            browserService.openBrowser(speedTestWebsite.webSiteUrl());
            browserService.centralizedWebPage(speedTestWebsite.centralized());

            if (speedTestWebsite.haveStartButton()) {
                clickStartTestButton(speedTestWebsite);
                moveMouseToStartingPoint();
            }

            logger.info("Start '" + speedTestWebsite.getIdentifier() + "' speed test");
            startMeasuringTimestamp = System.currentTimeMillis();
            waitForTestToFinish(speedTestWebsite);
            TimeUnit.MILLISECONDS.sleep(DELAY_TIME_BEFORE_SNAPSHOT);
            websiteSnapshot = takeScreenSnapshot();

            return new SpeedTestWebSiteDownloadInfo.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                                   .setSucceed()
                                                   .setStartDownloadingTimeSnapshot(startMeasuringTimestamp)
                                                   .setWebSiteDownloadInfoSnapshot(websiteSnapshot)
                                                   .build();

        } catch (BrowserFailedException e) {
            handleError("Failed to complete speed test " + speedTestWebsite.getIdentifier() + ", " + e.getMessage(), e);
            return new SpeedTestWebSiteDownloadInfo.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                                   .setFailure()
                                                   .setWebSiteDownloadInfoSnapshot(takeScreenSnapshot())
                                                   .build();

        } catch (WebDriverException | InterruptedException e) {
            handleError(e.getMessage(), e);
            throw new UserInterruptException(e.getMessage(), e);

        } catch (UnknownHostException e) {
            handleError(e.getMessage(), e);
            throw new ConnectionException(e.getMessage(), e);

        } catch(Exception e) {
            handleError(e.getMessage(), e);
            throw new WebSiteDownloadInfoException(e.getMessage());

        } finally {
            browserService.closeBrowser();
        }
    }

    private void clickStartTestButton(SpeedTestWebSite speedTestWebsite) throws BrowserFailedException, IOException, InterruptedException {
        if (speedTestWebsite.isFlashAble()) {
            browserService.pressFlashStartTestButton(speedTestWebsite.buttonIds());
        } else {
            browserService.pressStartButtonById(speedTestWebsite.buttonId());
        }
    }


    private void waitForTestToFinish(SpeedTestWebSite speedTestWebsite) throws BrowserFailedException, InterruptedException {
        if (speedTestWebsite.isFlashAble()) {
            browserService.waitForFlashTestToFinish(speedTestWebsite.finishIdentifiers());
        } else {
            browserService.waitForTestToFinishByText(speedTestWebsite.finishIdentifier(),
                                                     speedTestWebsite.finishTextIdentifier());
        }
    }

    @Override
    public void close() throws IOException {
        browserService.closeBrowser();
    }

    private void handleError(String errorMessage, Exception e) {
        dataAccessService.sendErrorMessage(errorMessage);
        logger.error(errorMessage, e);
    }
}
