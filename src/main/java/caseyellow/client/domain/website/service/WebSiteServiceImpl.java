package caseyellow.client.domain.website.service;

import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.exceptions.ConnectionException;
import caseyellow.client.exceptions.UserInterruptException;
import caseyellow.client.exceptions.WebSiteDownloadInfoException;
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

    public static final int DELAY_TIME_BEFORE_SNAPSHOT = 1200;

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
    public SpeedTestWebSite produceSpeedTestWebSite(final SpeedTestMetaData speedTestWebsite) throws UserInterruptException, ConnectionException {
        String result;
        String websiteSnapshot;
        long startMeasuringTimestamp;

        try {
            messagesService.showMessage("Start testing web site: " + speedTestWebsite.getWebSiteUrl());
            browserService.openBrowser(speedTestWebsite.getWebSiteUrl());
            browserService.centralizedWebPage(speedTestWebsite.getCentralized());

            if (speedTestWebsite.isHaveStartButton()) {
                clickStartTestButton(speedTestWebsite);
                moveMouseToStartingPoint();
            }

            logger.info("Start '" + speedTestWebsite.getIdentifier() + "' speed test");
            startMeasuringTimestamp = System.currentTimeMillis();
            result = waitForTestToFinish(speedTestWebsite);
            TimeUnit.MILLISECONDS.sleep(DELAY_TIME_BEFORE_SNAPSHOT);
            websiteSnapshot = takeScreenSnapshot();

            return new SpeedTestWebSite.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                       .setSucceed()
                                       .setStartDownloadingTimeSnapshot(startMeasuringTimestamp)
                                       .setWebSiteDownloadInfoSnapshot(websiteSnapshot)
                                       .setURL(speedTestWebsite.getWebSiteUrl())
                                       .setNonFlashResult(result)
                                       .build();

        } catch (BrowserFailedException e) {
            logger.error("Failed to complete speed test " + speedTestWebsite.getIdentifier() + ", " + e.getMessage(), e);
            return new SpeedTestWebSite.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                       .setFailure()
                                       .setNonFlashResult(e.getMessage())
                                       .setWebSiteDownloadInfoSnapshot(takeScreenSnapshot())
                                       .build();

        } catch (WebDriverException | InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new UserInterruptException(e.getMessage(), e);

        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionException(e.getMessage(), e);

        } catch(Exception e) {
            logger.error(e.getMessage(), e);
            throw new WebSiteDownloadInfoException(e.getMessage(), e);

        } finally {
            browserService.closeBrowser();
        }
    }

    private void clickStartTestButton(SpeedTestMetaData speedTestWebsite) throws BrowserFailedException, IOException, InterruptedException {
        if (speedTestWebsite.isFlashAble()) {
            browserService.pressFlashStartTestButton(speedTestWebsite.getSpeedTestFlashMetaData().getButtonIds());
        } else {
            browserService.pressStartButtonById(speedTestWebsite.getSpeedTestNonFlashMetaData().getButtonId());
        }
    }


    private String waitForTestToFinish(SpeedTestMetaData speedTestWebsite) throws BrowserFailedException, InterruptedException {
        if (speedTestWebsite.isFlashAble()) {
            return browserService.waitForFlashTestToFinish(speedTestWebsite.getSpeedTestFlashMetaData().getFinishIdentifier(),
                                                           speedTestWebsite.getSpeedTestFlashMetaData().getFinishIdentifiers(),
                                                           speedTestWebsite.getRoles());
        } else {
            return browserService.waitForTestToFinishByText(speedTestWebsite.getSpeedTestNonFlashMetaData().getFinishIdentifier(),
                                                            speedTestWebsite.getSpeedTestNonFlashMetaData());
        }
    }

    @Override
    public void close() throws IOException {
        browserService.closeBrowser();
    }
}
