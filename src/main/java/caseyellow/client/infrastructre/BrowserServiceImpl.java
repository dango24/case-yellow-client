package caseyellow.client.infrastructre;

import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.FindFailedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

import static caseyellow.client.common.Utils.getFileFromResources;

/**
 * Created by Dan on 6/30/2017.
 */
@Service
public class BrowserServiceImpl implements BrowserService {

    private Logger logger = Logger.getLogger(BrowserServiceImpl.class);

    @Value("${buttons-dir}")
    private String btnDir;

    @Value("${identifier-dir}")
    private String identifierDir;

    private WebDriver webDriver;

    public BrowserServiceImpl() {
        this.webDriver = new FirefoxDriver();
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
    public void pressTestButton(String btnImagePath) throws FindFailedException {

        try {
            File imgLocation = getFileFromResources(btnDir + btnImagePath);
            Screen screen = new Screen();

            screen.exists(imgLocation.getAbsolutePath());
            screen.click(imgLocation.getAbsolutePath());

        } catch (Exception e) {
            throw new FindFailedException(e.getMessage());
        }
    }

    @Override
    public void waitForTestToFinish(String identifierPath, int waitForTestToFinishInSec) throws FindFailedException {

        try {
            File done = getFileFromResources(identifierDir + identifierPath);
            Screen screen = new Screen();

            screen.wait(done.getAbsolutePath(), waitForTestToFinishInSec);
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
