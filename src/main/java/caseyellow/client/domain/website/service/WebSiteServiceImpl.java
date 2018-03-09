package caseyellow.client.domain.website.service;

import caseyellow.client.domain.data.access.DataAccessService;
import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.system.SystemService;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.model.SpeedTestResult;
import caseyellow.client.exceptions.*;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.browser.BrowserService;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.File;
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

    private SystemService systemService;
    private BrowserService browserService;
    private MessagesService messagesService;
    private DataAccessService dataAccessService;

    @Autowired
    public WebSiteServiceImpl(BrowserService browserService, MessagesService messagesService, DataAccessService dataAccessService, SystemService systemService) {
        this.browserService = browserService;
        this.messagesService = messagesService;
        this.dataAccessService = dataAccessService;
        this.systemService = systemService;
    }

    @Override
    public SpeedTestWebSite produceSpeedTestWebSite(final SpeedTestMetaData speedTestWebsite) throws UserInterruptException, ConnectionException {
        SpeedTestResult result;
        String websiteSnapshot;
        long startMeasuringTimestamp;

        try {
            messagesService.showMessage("Start testing web site: " + speedTestWebsite.getWebSiteUrl());
            logger.info("Start testing web site: " + speedTestWebsite.getWebSiteUrl());

            browserService.openBrowser(speedTestWebsite.getWebSiteUrl());
            browserService.centralizedWebPage(speedTestWebsite.getCentralized());

            if (speedTestWebsite.isHaveStartButton()) {
                clickStartTestButton(speedTestWebsite);
            }

            moveMouseToStartingPoint();
            startMeasuringTimestamp = System.currentTimeMillis();
            result = waitForTestToFinish(speedTestWebsite);
            TimeUnit.MILLISECONDS.sleep(DELAY_TIME_BEFORE_SNAPSHOT);
            websiteSnapshot = takeScreenSnapshot();

            return new SpeedTestWebSite.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                       .setSucceed()
                                       .setStartDownloadingTimeSnapshot(startMeasuringTimestamp)
                                       .setWebSiteDownloadInfoSnapshot(result.getSnapshot())
                                       .setURL(speedTestWebsite.getWebSiteUrl())
                                       .setNonFlashResult(result.getResult())
                                       .setMD5(systemService.convertToMD5(new File(websiteSnapshot)))
                                       .build();

        } catch (BrowserFailedException e) {
            return handleProduceSpeedTestWebSiteFailure(speedTestWebsite, takeScreenSnapshot(), e);

        } catch (AnalyzeException e) {
            return handleProduceSpeedTestWebSiteFailure(speedTestWebsite, e.getSnapshot(), e);

        } catch (WebDriverException | InterruptedException e) {
            logger.error(String.format("InterruptedException, Failed to produce SpeedTestWebSite, error: %s", e.getMessage()), e);
            throw new UserInterruptException(e.getMessage(), e);

        } catch (UnknownHostException e) {
            logger.error(String.format("Failed to produce SpeedTestWebSite, error: %s", e.getMessage()), e);
            throw new ConnectionException(e.getMessage(), e);

        } catch(Exception e) {
            logger.error(String.format("Failed to produce SpeedTestWebSite, error: %s", e.getMessage()), e);
            throw new WebSiteDownloadInfoException(e.getMessage(), e);

        } finally {
            close();
        }
    }

    private SpeedTestWebSite handleProduceSpeedTestWebSiteFailure(SpeedTestMetaData speedTestWebsite, String failedTestSnapshot, Exception e) {
        logger.error("Failed to complete speed test " + speedTestWebsite.getIdentifier() + ", " + e.getMessage(), e);

        return new SpeedTestWebSite.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                   .setFailure()
                                   .setMessage(e.getMessage())
                                   .setWebSiteDownloadInfoSnapshot(failedTestSnapshot)
                                   .setMD5(systemService.convertToMD5(new File(failedTestSnapshot)))
                                   .build();
    }

    private void clickStartTestButton(SpeedTestMetaData speedTestWebsite) throws BrowserFailedException, IOException, InterruptedException, AnalyzeException {
        if (speedTestWebsite.isFlashAble()) {
            browserService.pressFlashStartTestButton(speedTestWebsite.getIdentifier(), speedTestWebsite.getSpeedTestFlashMetaData().getButtonIds(), speedTestWebsite.getSpeedTestFlashMetaData().getMaxAttempts());
        } else {
            browserService.pressStartButtonById(speedTestWebsite.getSpeedTestNonFlashMetaData().getButtonId());
        }
    }

    private SpeedTestResult waitForTestToFinish(SpeedTestMetaData speedTestWebsite) throws BrowserFailedException, InterruptedException, AnalyzeException {
        if (speedTestWebsite.isFlashAble()) {
            speedTestWebsite.resetAllRules();
            return browserService.waitForFlashTestToFinish(speedTestWebsite.getIdentifier(),
                                                           speedTestWebsite.getSpeedTestFlashMetaData().getFinishIdentifier(),
                                                           speedTestWebsite.getSpeedTestFlashMetaData().getFinishIdentifiers(),
                                                           speedTestWebsite.getRoles());
        } else {
            return browserService.waitForTestToFinishByText(speedTestWebsite.getSpeedTestNonFlashMetaData().getFinishIdentifier(),
                                                            speedTestWebsite.getSpeedTestNonFlashMetaData());
        }
    }

    @Override
    public void close() {
        browserService.closeBrowser();
    }
}
