package caseyellow.client.infrastructre;

import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.FindFailedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.sikuli.script.Screen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static caseyellow.client.common.Utils.getImgFromResources;

/**
 * Created by Dan on 6/30/2017.
 */
@Service
public class BrowserServiceImpl implements BrowserService {

    @Value("${waitForTestToFinishInSec}")
    private final int waitForTestToFinishInSec = 120;
    private Logger logger = Logger.getLogger(BrowserServiceImpl.class);

    @Value("${buttons-dir}")
    private String btnDir;

    @Value("${identifier-dir}")
    private String identifierDir;

    private WebDriver webDriver;
    private int additionTimeForWebTestToFinish;

    public BrowserServiceImpl() {
        this.webDriver = new FirefoxDriver();
        additionTimeForWebTestToFinish = 0;
    }

    @Override
    public void openBrowser(String url) {

        try {
            webDriver.get(url);

        } catch (UnreachableBrowserException e) {
            logger.error("Failed to open browser, reattempt again with new driver");
            webDriver = new FirefoxDriver();
            webDriver.get(url);
        }

        webDriver.manage().window().maximize();
    }

    @Override
    public void closeBrowser() {
        webDriver.close();
    }

    @Override
    public void addAdditionalTimeForWebTestToFinish(int additionTimeInSec) {
        this.additionTimeForWebTestToFinish = additionTimeInSec;
    }

    @Override
    public void pressTestButton(String webSiteBtnIdentifier) throws FindFailedException {

        try {
            String imgLocation = getImgFromResources(btnDir + webSiteBtnIdentifier);
            Screen screen = new Screen();

            screen.exists(imgLocation);
            screen.click(imgLocation);

        } catch (Exception e) {
            throw new FindFailedException(e.getMessage());
        }
    }

    @Override
    public void waitForTestToFinish(String imgIdentifier) throws FindFailedException {

        try {
            String testFinishIdentifierImg = getImgFromResources(identifierDir + imgIdentifier);
            Screen screen = new Screen();

            screen.wait(testFinishIdentifierImg, waitForTestToFinishInSec + additionTimeForWebTestToFinish);
        } catch (Exception e) {
            throw new FindFailedException(e.getMessage());
        }
    }

    @Override
    public String takeScreenSnapshot() {
        Screen screen = new Screen();
        return screen.capture().getFile();
    }

    @Override
    public String getBrowserName() {
        return ((RemoteWebDriver)webDriver).getCapabilities().getBrowserName();
    }
}
