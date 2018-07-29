package caseyellow.client.domain.website.service;

import caseyellow.client.sevices.gateway.services.DataAccessService;
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

import static caseyellow.client.common.FileUtils.takeScreenSnapshot;
import static caseyellow.client.common.Utils.moveMouseToStartingPoint;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class WebSiteServiceImpl implements WebSiteService, Closeable {

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
        long startMeasuringTimestamp;

        try {
            messagesService.showMessage("Start testing web site: " + speedTestWebsite.getWebSiteUrl());
            logger.info("Start testing web site: " + speedTestWebsite.getWebSiteUrl());

            browserService.openBrowser(speedTestWebsite.getWebSiteUrl());
            browserService.centralizedWebPage(speedTestWebsite.getCentralized());
            moveMouseToStartingPoint();

            if (speedTestWebsite.isHaveStartButton()) {
                clickStartTestButton(speedTestWebsite);
            }

            moveMouseToStartingPoint();
            startMeasuringTimestamp = System.currentTimeMillis();
            result = waitForTestToFinish(speedTestWebsite);

            return new SpeedTestWebSite.SpeedTestWebSiteDownloadInfoBuilder(speedTestWebsite.getIdentifier())
                                       .setSucceed()
                                       .setStartDownloadingTimeSnapshot(startMeasuringTimestamp)
                                       .setWebSiteDownloadInfoSnapshot(result.getSnapshot())
                                       .setURL(speedTestWebsite.getWebSiteUrl())
                                       .setNonFlashResult(result.getResult())
                                       .setMD5(systemService.convertToMD5(new File(result.getSnapshot())))
                                       .build();

        } catch (BrowserFailedException | WebDriverException e) {
            return handleProduceSpeedTestWebSiteFailure(speedTestWebsite, takeScreenSnapshot().getAbsolutePath(), e);

        } catch (AnalyzeException e) {
            return handleProduceSpeedTestWebSiteFailure(speedTestWebsite, e.getSnapshot(), e);

        } catch (InterruptedException e) {
            logger.error(String.format("InterruptedException, Failed to produce SpeedTestWebSite, error: %s", e.getMessage()));
            throw new UserInterruptException(e.getMessage(), e);

        } catch (UnknownHostException | ConnectionException e) {
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
            browserService.pressFlashStartTestButton(speedTestWebsite.getIdentifier(),speedTestWebsite.getSpeedTestFlashMetaData());
        } else {
            browserService.pressStartButtonById(speedTestWebsite.getIdentifier(), speedTestWebsite.getSpeedTestNonFlashMetaData().getButtonId());
        }
    }

    private SpeedTestResult waitForTestToFinish(SpeedTestMetaData speedTestWebsite) throws BrowserFailedException, InterruptedException, AnalyzeException {
        speedTestWebsite.resetAllRules();

        if (speedTestWebsite.isFlashAble()) {
            return browserService.waitForFlashTestToFinish(speedTestWebsite.getIdentifier(),
                                                           speedTestWebsite.getSpeedTestFlashMetaData().getFinishIdentifier(),
                                                           speedTestWebsite.getRoles(),
                                                           speedTestWebsite.getSpeedTestFlashMetaData().getImageCenterPoint());
        } else {
            return browserService.waitForTestToFinishByText(speedTestWebsite.getIdentifier(), speedTestWebsite.getRoles());
        }
    }

    @Override
    public void close() {
        browserService.closeBrowser();
    }

    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }
}
