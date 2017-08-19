package caseyellow.client.domain.browser;

import caseyellow.client.common.Mapper;
import caseyellow.client.common.Point;
import caseyellow.client.common.Utils;
import caseyellow.client.domain.analyze.model.DescriptionMatch;
import caseyellow.client.domain.analyze.model.WordIdentifier;
import caseyellow.client.domain.analyze.service.TextAnalyzerService;
import caseyellow.client.exceptions.*;
import caseyellow.client.infrastructre.image.recognition.*;
import caseyellow.client.domain.interfaces.OcrService;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static caseyellow.client.common.Mapper.USER_INTERRUPT_CODE;
import static caseyellow.client.common.Utils.*;
import static com.google.common.base.Preconditions.checkNotNull;
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

    private WebDriver webDriver;
    private OcrService ocrService;
    private TextAnalyzerService textAnalyzer;

    @PostConstruct
    public void init() throws IOException {
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
        ocrService.cancelRequest();
    }

    @Override
    public void pressStartButtonById(String btnId) throws BrowserFailedException {
        checkBrowser();

        try {
            TimeUnit.MILLISECONDS.sleep(900);
            webDriver.findElement(By.id(btnId)).click();

        } catch (WebDriverException e) {
            throw new UserInterruptException(e.getMessage(), e);

        } catch (Exception e) {
            throw new BrowserFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void pressFlashStartTestButton(Set<WordIdentifier> webSiteBtnIdentifiers) throws BrowserFailedException, UserInterruptException, IOException, InterruptedException {
        checkBrowser();

        try {
            int waitForTestToFinishInSec = getWaitForTestToFinishInSec(waitForStartButton);
            int numOfAttempts = waitForStartTestButtonToAppearInSec / waitForTestToFinishInSec;
            waitForImageAppearance(webSiteBtnIdentifiers, numOfAttempts, waitForTestToFinishInSec, true);

        } catch (WebDriverException e) {
            throw new UserInterruptException(e.getMessage(), e);
        } catch (RequestFailureException e) {
            throw new BrowserFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void waitForFlashTestToFinish(Set<WordIdentifier> identifiers) throws BrowserFailedException, UserInterruptException {
        checkBrowser();

        try {
            int waitForTestToFinishInterval = getWaitForTestToFinishInSec(waitForFinishIdentifier);
            int numOfAttempts = waitForTestToFinishInSec / waitForTestToFinishInterval;

            waitForImageAppearance(identifiers, numOfAttempts, waitForFinishIdentifier, false);

        } catch (WebDriverException e) {
            logger.error(e.getMessage());
            throw new UserInterruptException(e.getMessage(), e);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BrowserFailedException(e.getMessage(), e);
        }
    }

    @Override
    public boolean waitForTestToFinishByText(String identifier, String finishTextIdentifier) throws BrowserFailedException, InterruptedException {
        int currentAttempt = 0;
        int numOfAttempts = waitForTestToFinishInSec / (int)TimeUnit.MILLISECONDS.toSeconds(waitForFinishIdentifier);
        By by = getByIdentifier(identifier);

        do {
            checkBrowser();

            if (webDriver.findElement(by).getText().equals(finishTextIdentifier)) {
                return true;
            }

            TimeUnit.MILLISECONDS.sleep(waitForFinishIdentifier);

        } while (++currentAttempt < numOfAttempts);

        throw new BrowserFailedException("Failure to find finish test identifier : " + identifier + " with text: " + finishTextIdentifier);
    }

    private By getByIdentifier(String identifier) {
        String[] identifiers = identifier.split("=");
        return identifiers[0].equals("id") ? By.id(identifiers[1]) : By.className(identifiers[1]);
    }

    private void checkBrowser() throws BrowserFailedException {
        try {
            webDriver.getTitle(); // Will throw UnreachableBrowserException if browser is closed

        } catch (UnreachableBrowserException | NoSuchSessionException e) {
            throw new UserInterruptException(e.getMessage(), e);
        } catch (Exception e) {
            throw new BrowserFailedException("Browser is closed, " + e.getMessage(), e);
        }
    }

    private int getWaitForTestToFinishInSec(int waitForStartButton) {
        return waitForStartButton < 1000 ? 1 : (int) TimeUnit.MILLISECONDS.toSeconds(waitForStartButton);
    }

    private boolean waitForImageAppearance(Set<WordIdentifier> textIdentifiers, int numOfAttempts , int waitForImageInSec, boolean clickImage) throws IOException, InterruptedException, BrowserFailedException, RequestFailureException {
        int currentAttempt = 0;

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
            } catch (OcrParsingException e) {
                logger.error("OCR parsing failed, try new attempt, " + e.getMessage(), e);
            }

        } while (++currentAttempt < numOfAttempts);

        throw new BrowserFailedException("Failure to find finish test identifiers: " + textIdentifiers);
    }

    private boolean foundMatchingDescription(Set<WordIdentifier> textIdentifiers, boolean clickImage) throws IOException, AnalyzeException, OcrParsingException, RequestFailureException {
        Point point;
        OcrResponse ocrResponse = ocrService.parseImage(takeScreenSnapshot());
        checkNotNull(ocrResponse, "Ocr response is null");
        DescriptionMatch matchDescription = textAnalyzer.isDescriptionExist(textIdentifiers, ocrResponse.getTextAnnotations());
        checkNotNull(matchDescription, "Match Description is null");
        checkNotNull(matchDescription.getDescriptionLocation(), "Match Description location is null");

        if (matchDescription.foundMatchedDescription()) {

            if (clickImage) {
                point = matchDescription.getDescriptionLocation().getCenter();
                checkNotNull(point, "Matched description center point is null");
                click(point.getX(), point.getY());
            }

            return true;

        } else {
            return false;
        }
    }

    @Override
    public void centralizedWebPage(int centralized) throws InterruptedException, BrowserFailedException {
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