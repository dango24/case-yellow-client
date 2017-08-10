package caseyellow.client.infrastructre;

import caseyellow.client.common.Mapper;
import caseyellow.client.common.Utils;
import caseyellow.client.common.resolution.ResolutionProperties;
import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.BrowserCommandFailedException;
import caseyellow.client.exceptions.UserInterruptException;
import caseyellow.client.infrastructre.image.comparison.ImageComparison;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static caseyellow.client.common.Utils.*;
import static java.lang.Math.toIntExact;

/**
 * Created by Dan on 6/30/2017.
 */
@Component
public class BrowserServiceImpl implements BrowserService {

    private Logger logger = Logger.getLogger(BrowserServiceImpl.class);

    @Value("${wait-for-start-test-button-to-appear-in-sec}")
    private final int waitForStartTestButtonToAppearInSec = 20;

    @Value("${wait-for-test-to-finish-in-sec}")
    private final int waitForTestToFinishInSec = 120;

    @Value("${wait-for-start-button}")
    private final int waitForStartButton = 2;

    @Value("${wait-For-Finish-identifier}")
    private final int waitForFinishIdentifier = 10;

    @Value("${buttons-dir}")
    private String btnDir;

    @Value("${identifier-dir}")
    private String identifierDir;

    @Value("${chromeDriverExecutorPath}")
    private String chromeDriverExecutorPath;

    private Mapper mapper;
    private WebDriver webDriver;
    private ImageComparison imageComparison;

    public BrowserServiceImpl() throws IOException {
        initWebDriver();
        closeBrowser();
    }

    private void initWebDriver() throws IOException {
        String chromeDriver = getTempFileFromResources("drivers/chromedriver.exe").getAbsolutePath();
        System.setProperty("webdriver.chrome.driver", chromeDriver);
        this.webDriver = new ChromeDriver();
    }

    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setImageComparison(ImageComparison imageComparison) {
        this.imageComparison = imageComparison;
    }

    @Override
    public void openBrowser(String url) throws IOException {

        try {
            webDriver.get(url);

        } catch (Exception e) {
            logger.warn("Failed to open browser, reattempt again with new driver" + e);
            initWebDriver();
            webDriver.get(url);
        }

        webDriver.manage().window().maximize();
    }

    @Override
    public void closeBrowser() {
        webDriver.quit();
    }

    @Override
    public void pressStartTestButton(String webSiteBtnIdentifier) throws BrowserCommandFailedException, UserInterruptException {
        checkBrowser();

        try {
            String btnIdentifierImgPath = getImgFromResources(btnDir + webSiteBtnIdentifier);
            ResolutionProperties startButtonProperties = mapper.getStartButtonResolutionProperties(webSiteBtnIdentifier);
            int numOfAttempts = waitForStartTestButtonToAppearInSec / waitForStartButton;

            TimeUnit.MILLISECONDS.sleep(700);

            waitForImageAppearance(btnIdentifierImgPath, startButtonProperties, numOfAttempts, waitForStartButton);

            click(startButtonProperties.getCenter().getX(),
                  startButtonProperties.getCenter().getY());

        } catch (WebDriverException e) {
            throw new UserInterruptException(e.getMessage(), e);

        } catch (Exception e) {
            throw new BrowserCommandFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void waitForTestToFinish(String imgIdentifier) throws BrowserCommandFailedException, UserInterruptException {
        checkBrowser();

        try {
            String testFinishIdentifierImg = getImgFromResources(identifierDir + imgIdentifier);
            ResolutionProperties finishTestIdentifierProperties = mapper.getFinishIdentifierImg(imgIdentifier);
            int numOfAttempts = waitForTestToFinishInSec / waitForFinishIdentifier;

            waitForImageAppearance(testFinishIdentifierImg, finishTestIdentifierProperties, numOfAttempts, waitForFinishIdentifier);

        } catch (WebDriverException e) {
            logger.error(e.getMessage());
            throw new UserInterruptException(e.getMessage());

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BrowserCommandFailedException(e.getMessage());
        }
    }

    private void checkBrowser() {
        webDriver.getTitle(); // Will throw an exception if browser is closed
    }

    private boolean waitForImageAppearance(String btnIdentifierImgPath, ResolutionProperties resolutionProperties,
                                           int numOfAttempts , int waitForImageInSec) throws IOException, InterruptedException, BrowserCommandFailedException {
        int currentAttempt = 0;

        do {
            checkBrowser();
            String screenshot = takeScreenSnapshot();
            String subImagePath = getSubImageFile(resolutionProperties, screenshot);

            if (imageComparison.compare(subImagePath, btnIdentifierImgPath, resolutionProperties.getComparisionThreshold())) {
                return true;
            }

            TimeUnit.SECONDS.sleep(waitForImageInSec);

        } while (++currentAttempt < numOfAttempts);

        throw new BrowserCommandFailedException("Failure to find start button for btn image path: " + btnIdentifierImgPath);
    }

    private String getSubImageFile(ResolutionProperties resolutionProperties, String screenshot) throws IOException {
        return Utils.getSubImageFile(resolutionProperties.getX(),// -70,
                                     resolutionProperties.getY(),// - 100,
                                     resolutionProperties.getW(),// + 100,
                                     resolutionProperties.getH(),// + 150,
                                     screenshot).getAbsolutePath();
    }

    @Override
    public void centralizedWebPage(String identifier) {
        checkBrowser();
        String screenResolution = Utils.getScreenResolution();
        long scrollDownPixel = mapper.getPixelScrollDown(identifier, screenResolution);

        if (scrollDownPixel > 0) {
            scrollDown(toIntExact(scrollDownPixel));
        }
    }

    private void scrollDown(int scrollDownPixel) {
        JavascriptExecutor jse = (JavascriptExecutor)webDriver;
        jse.executeScript("window.scrollBy(0," + scrollDownPixel + ")", "");
    }

    public void click(int x, int y) throws AWTException {
        Robot bot = new Robot();
        bot.mouseMove(x, y);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
