package caseyellow.client.infrastructre;

import caseyellow.client.common.Mapper;
import caseyellow.client.common.Utils;
import caseyellow.client.common.resolution.Point;
import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.BrowserCommandFailedException;
import caseyellow.client.exceptions.UserInterruptException;
import caseyellow.client.infrastructre.image.comparison.*;
import caseyellow.client.domain.interfaces.OcrService;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    public void pressFlashStartTestButton(String webSiteBtnIdentifier) throws BrowserCommandFailedException, UserInterruptException {
        checkBrowser();

        try {
            int waitForTestToFinishInSec = waitForStartButton < 1000 ? 1 : (int)TimeUnit.SECONDS.toSeconds(waitForStartButton);
            int numOfAttempts = waitForStartTestButtonToAppearInSec / waitForTestToFinishInSec;
            waitForImageAppearance(webSiteBtnIdentifier, numOfAttempts, waitForTestToFinishInSec, true);

        } catch (WebDriverException e) {
            throw new UserInterruptException(e.getMessage(), e);

        } catch (Exception e) {
            throw new BrowserCommandFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void waitForFlashTestToFinish(String identifier) throws BrowserCommandFailedException, UserInterruptException {
        checkBrowser();

        try {
            int numOfAttempts = waitForTestToFinishInSec / waitForFinishIdentifier;

            waitForImageAppearance(identifier, numOfAttempts, waitForFinishIdentifier, false);

        } catch (WebDriverException e) {
            logger.error(e.getMessage());
            throw new UserInterruptException(e.getMessage());

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new BrowserCommandFailedException(e.getMessage());
        }
    }

    @Override
    public boolean waitForTestToFinishByText(String identifier, String finishTextIdentifier) throws BrowserCommandFailedException, InterruptedException {
        int currentAttempt = 0;
        int numOfAttempts = waitForTestToFinishInSec / waitForFinishIdentifier;
        String[] identifiers = identifier.split("=");
        By by = identifiers[0].equals("id") ? By.id(identifiers[1]) : By.className(identifiers[1]);

        do {
            checkBrowser();

            if (webDriver.findElement(by).getText().equals(finishTextIdentifier)) {
                return true;
            }

            TimeUnit.SECONDS.sleep(waitForFinishIdentifier);

        } while (++currentAttempt < numOfAttempts);

        throw new BrowserCommandFailedException("Failure to find finish test identifier : " + identifier + " with text: " + finishTextIdentifier);
    }

    private void checkBrowser() {
        webDriver.getTitle(); // Will throw an exception if browser is closed
    }

    private boolean waitForImageAppearance(String textIdentifier, int numOfAttempts , int waitForImageInSec, boolean clickImage) throws IOException, InterruptedException, BrowserCommandFailedException {
        OcrResponse ocrResponse;
        int currentAttempt = 0;

        do {
            checkBrowser();
            TimeUnit.MILLISECONDS.sleep(waitForImageInSec);
            ocrResponse = ocrService.parseImage(takeScreenSnapshot());

            if (isTextIdentifierExist(textIdentifier, ocrResponse, clickImage)) {
                return true;
            }

        } while (++currentAttempt < numOfAttempts);

        logger.warn("Failure to find finish test identifier: " + textIdentifier);
        logger.info("Assume the test finish properly");

        return false;
    }

    private boolean isTextIdentifierExist(String textIdentifier, OcrResponse ocrResponse, boolean clickImage) {
        Optional<WordData> wordDataOptional = ocrResponse.getTextAnnotations()
                                                         .stream()
                                                         .filter(word -> word.getDescription().equals(textIdentifier))
                                                         .findFirst();
        if (wordDataOptional.isPresent()) {

            if (clickImage) {
                clickImage(wordDataOptional.get());
            }

            return true;

        } else {
            return false;
        }
    }

    private void clickImage(WordData wordData) {
        List<Point> vertices = wordData.getBoundingPoly().getVertices();
        int minX = Utils.getMinX(vertices);
        int minY = Utils.getMinY(vertices);
        int maxX = Utils.getMaxX(vertices);
        int maxY = Utils.getMaxY(vertices);

        Point center = new Point( (minX + maxX)/2, (minY + maxY)/2);
        click(center.getX(), center.getY());
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
}