package caseyellow.client.infrastructre;

import caseyellow.client.common.Mapper;
import caseyellow.client.common.Point;
import caseyellow.client.domain.analyze.model.DescriptionMatch;
import caseyellow.client.domain.analyze.model.WordIdentifier;
import caseyellow.client.domain.analyze.service.TextAnalyzerService;
import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.AnalyzeException;
import caseyellow.client.exceptions.BrowserCommandFailedException;
import caseyellow.client.exceptions.UserInterruptException;
import caseyellow.client.infrastructre.image.recognition.*;
import caseyellow.client.domain.interfaces.OcrService;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Set;
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


    @Value("${wait-for-start-button}")
    private final int waitForStartButton = 900;

    @Value("${wait-for-test-to-finish-in-sec}")
    private final int waitForTestToFinishInSec = 120;

    @Value("${wait-For-Finish-identifier}")
    private final int waitForFinishIdentifier = 10_000;

    @Value("${chromeDriverExecutorPath}")
    private String chromeDriverExecutorPath;

    private Mapper mapper;
    private WebDriver webDriver;
    private OcrService ocrService;
    private TextAnalyzerService textAnalyzer;

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
    public void setOcrService(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setTextAnalyzer(TextAnalyzerService textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
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
    public void pressStartButtonById(String btnId) throws BrowserCommandFailedException {
        checkBrowser();

        try {
            TimeUnit.MILLISECONDS.sleep(900);
            webDriver.findElement(By.id(btnId)).click();

        } catch (WebDriverException e) {
            throw new UserInterruptException(e.getMessage(), e);

        } catch (Exception e) {
            throw new BrowserCommandFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void pressFlashStartTestButton(Set<WordIdentifier> webSiteBtnIdentifiers) throws BrowserCommandFailedException, UserInterruptException, IOException, InterruptedException {
        checkBrowser();

        try {
            int waitForTestToFinishInSec = waitForStartButton < 1000 ? 1 : (int)TimeUnit.MILLISECONDS.toSeconds(waitForStartButton);
            int numOfAttempts = waitForStartTestButtonToAppearInSec / waitForTestToFinishInSec;
            waitForImageAppearance(webSiteBtnIdentifiers, numOfAttempts, waitForTestToFinishInSec, true);

        } catch (WebDriverException e) {
            throw new UserInterruptException(e.getMessage(), e);

        }
    }

    @Override
    public void waitForFlashTestToFinish(Set<WordIdentifier> identifiers) throws BrowserCommandFailedException, UserInterruptException {
        checkBrowser();

        try {
            int waitForTestToFinishInterval = waitForFinishIdentifier < 1000 ? 1 : (int)TimeUnit.MILLISECONDS.toSeconds(waitForFinishIdentifier);
            int numOfAttempts = waitForTestToFinishInSec / waitForTestToFinishInterval;

            waitForImageAppearance(identifiers, numOfAttempts, waitForFinishIdentifier, false);

        } catch (WebDriverException e) {
            logger.error(e.getMessage());
            throw new UserInterruptException(e.getMessage(), e);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BrowserCommandFailedException(e.getMessage(), e);
        }
    }

    @Override
    public boolean waitForTestToFinishByText(String identifier, String finishTextIdentifier) throws BrowserCommandFailedException, InterruptedException {
        int currentAttempt = 0;
        int numOfAttempts = waitForTestToFinishInSec / (int)TimeUnit.MILLISECONDS.toSeconds(waitForFinishIdentifier);
        String[] identifiers = identifier.split("=");
        By by = identifiers[0].equals("id") ? By.id(identifiers[1]) : By.className(identifiers[1]);

        do {
            checkBrowser();

            if (webDriver.findElement(by).getText().equals(finishTextIdentifier)) {
                return true;
            }

            TimeUnit.MILLISECONDS.sleep(waitForFinishIdentifier);

        } while (++currentAttempt < numOfAttempts);

        throw new BrowserCommandFailedException("Failure to find finish test identifier : " + identifier + " with text: " + finishTextIdentifier);
    }

    private void checkBrowser() {
        webDriver.getTitle(); // Will throw an exception if browser is closed
    }

    private boolean waitForImageAppearance(Set<WordIdentifier> textIdentifiers, int numOfAttempts , int waitForImageInSec, boolean clickImage) throws IOException, InterruptedException, BrowserCommandFailedException {
        int currentAttempt = 0;
        Point point;
        OcrResponse ocrResponse;

        do {
            try {
                checkBrowser();
                TimeUnit.MILLISECONDS.sleep(waitForImageInSec);

                if (foundMatchingDescription(textIdentifiers, clickImage)) {
                    return true;
                }

            } catch (SocketTimeoutException e) {
                logger.error("Reached socket timeout, try new attempt, " + e.getMessage(), e);
            } catch (AnalyzeException e) {
                logger.error("Analyze failed, try new attempt, " + e.getMessage(), e);
            }

        } while (++currentAttempt < numOfAttempts);

        logger.warn("Failure to find finish test identifiers: " + textIdentifiers);
        logger.info("Assume the test finish properly");

        return false;
    }

    private Boolean foundMatchingDescription(Set<WordIdentifier> textIdentifiers, boolean clickImage) throws IOException, AnalyzeException {
        Point point;
        OcrResponse ocrResponse;
        ocrResponse = ocrService.parseImage(takeScreenSnapshot());
        DescriptionMatch matchDescription = textAnalyzer.isDescriptionExist(textIdentifiers, ocrResponse.getTextAnnotations());

        if (matchDescription.foundMatchedDescription()) {

            if (clickImage) {
                point = matchDescription.getDescriptionLocation().getCenter();
                click(point.getX(), point.getY());
            }

            return true;
        }
        return null;
    }

    @Override
    public void centralizedWebPage(int centralized) throws InterruptedException {
        checkBrowser();

        if (centralized > 0) {
            scrollDown(toIntExact(centralized));
            TimeUnit.MILLISECONDS.sleep(1300);
        }
    }

    private void scrollDown(int scrollDownPixel) {
        JavascriptExecutor jse = (JavascriptExecutor)webDriver;
        jse.executeScript("window.scrollBy(0," + scrollDownPixel + ")", "");
    }
}