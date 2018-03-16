package caseyellow.client.domain.browser;

import caseyellow.client.common.Utils;
import caseyellow.client.domain.analyze.model.*;
import caseyellow.client.domain.analyze.model.Point;
import caseyellow.client.domain.analyze.service.TextAnalyzerService;
import caseyellow.client.domain.system.SystemService;
import caseyellow.client.domain.website.model.Command;
import caseyellow.client.domain.website.model.Role;
import caseyellow.client.domain.website.model.SpeedTestNonFlashMetaData;
import caseyellow.client.domain.website.model.SpeedTestResult;
import caseyellow.client.exceptions.*;
import caseyellow.client.domain.analyze.service.ImageParsingService;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static caseyellow.client.common.Utils.*;
import static caseyellow.client.domain.analyze.model.ImageClassificationStatus.END;
import static caseyellow.client.domain.analyze.model.ImageClassificationStatus.START;
import static caseyellow.client.domain.analyze.model.ImageClassificationStatus.UNREADY;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.sun.jna.Platform.isLinux;
import static com.sun.jna.Platform.isWindows;
import static java.lang.Math.toIntExact;
import static java.util.Objects.nonNull;

/**
 * Created by Dan on 6/30/2017.
 */
@Component
public class BrowserServiceImpl implements BrowserService {

    private static final String GET_HTML_JS = "return document.getElementsByTagName('html')[0].innerHTML";

    private Logger logger = Logger.getLogger(BrowserServiceImpl.class);

    @Value("${wait-for-start-test-button-to-appear-in-sec}")
    private final int waitForStartTestButtonToAppearInSec = 20;

    @Value("${wait-for-start-button}")
    private final int waitForStartButton = 900;

    @Value("${wait-for-test-to-finish-in-sec}")
    private final int waitForTestToFinishInSec = 120;

    @Value("${wait-For-Finish-identifier}")
    private final int waitForFinishIdentifier = 10_000;

    private String logPath;
    private WebDriver webDriver;
    private SystemService systemService;
    private TextAnalyzerService textAnalyzer;
    private ImageParsingService imageParsingService;

    @Autowired
    public BrowserServiceImpl(ImageParsingService imageParsingService, TextAnalyzerService textAnalyzer, SystemService systemService) {
        this.textAnalyzer = textAnalyzer;
        this.systemService = systemService;
        this.imageParsingService = imageParsingService;
    }

    @PostConstruct
    public void init() throws IOException {
        initWebDriver();
        closeBrowser();
    }

    private void initWebDriver() throws IOException {
        String driverPath = getDriverPath();
        String chromeDriver = getTempFileFromResources(driverPath).getAbsolutePath();
        System.setProperty("webdriver.chrome.driver", chromeDriver);

        if (isLinux()) {
            systemService.runCommand("chmod +x " + chromeDriver);
        }

        this.webDriver = new ChromeDriver(generateChromeOptions());
    }

    private ChromeOptions generateChromeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        ChromeOptions options = new ChromeOptions();

        logPath = new File(Utils.createTmpDir(), "log_net").getAbsolutePath();

        String log_flag = "--log-net-log=" + logPath;
        prefs.put("profile.default_content_setting_values.plugins", 1);
        prefs.put("profile.content_settings.plugin_whitelist.adobe-flash-player", 1);
        prefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);
        // Enable Flash for this site
        prefs.put("PluginsAllowedForUrls", "https://*");

        ClassLoader classLoader = getClass().getClassLoader();
        File flashPath = new File(classLoader.getResource("libpepflashplayer.so").getFile());

        options.addArguments("--ppapi-flash-path=" + flashPath);


        options.setExperimentalOption("prefs", prefs);
        options.addArguments(log_flag);
        options.addArguments("disable-infobars");
        options.setExperimentalOption("prefs", prefs);

        return options;
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

        maximizeBrowserWindow();
    }

    private void maximizeBrowserWindow() {
        webDriver.manage().window().maximize();
    }

    @Override
    public void closeBrowser() {
        logger.warn("Close browser");
        webDriver.close();
    }

    @Override
    public void pressStartButtonById(String btnIdentifier) throws BrowserFailedException {
        checkBrowser();
        try {
            TimeUnit.MILLISECONDS.sleep(900);
            By by = getByIdentifier(btnIdentifier);

            WebDriverWait wait = new WebDriverWait(webDriver, 45);
            wait.until(ExpectedConditions.elementToBeClickable(by));
            WebElement webElement = webDriver.findElement(by);
            webElement.click();

        } catch (WebDriverException e) {
            throw new UserInterruptException(e.getMessage(), e);

        } catch (Exception e) {
            throw new BrowserFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void pressFlashStartTestButton(String identifier, Set<WordIdentifier> webSiteBtnIdentifiers, int maxAttempts) throws BrowserFailedException, UserInterruptException, InterruptedException, AnalyzeException {
        checkBrowser();

        try {
            int waitForTestToFinishInSec = getWaitForTestToFinishInSec(waitForStartButton);
            int numOfAttempts = waitForStartTestButtonToAppearInSec / waitForTestToFinishInSec;

            waitForImageAppearanceByImageClassification(identifier, true);
            waitForImageAppearanceByImageAnalyze(webSiteBtnIdentifiers, numOfAttempts, waitForTestToFinishInSec, true, "Start button");

        } catch (WebDriverException e) {
            throw new UserInterruptException(e.getMessage(), e);
        } catch (RequestFailureException | IOException e) {
            throw new BrowserFailedException(e.getMessage(), e);
        }
    }
    
    private SpeedTestResult waitForImageAppearanceByImageClassification(String identifier, boolean isStartState) throws AnalyzeException {
        String md5 = null;
        String screenshot = null;
        VisionRequest visionRequest;
        int attempt = 0;
        long imageClassificationTimeout = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2);

        try {
            while(System.currentTimeMillis() < imageClassificationTimeout) {

                screenshot = takeScreenSnapshot();
                md5 = systemService.convertToMD5(new File(screenshot));
                visionRequest = new VisionRequest(screenshot, md5);
                logger.info(String.format("Start classify image: %s", md5));

                ImageClassificationResult imageClassificationResult = imageParsingService.classifyImage(identifier, visionRequest);
                ImageClassificationStatus status = imageClassificationResult.getStatus();
                logger.info(String.format("ImageClassificationResult for identifier: %s, md5: %s, details: %s", identifier, md5, imageClassificationResult));

                switch (status) {
                    case START:
                    case END:
                        if (handleExistStatus(identifier, status, isStartState)) {
                            return new SpeedTestResult("SUCCESS", screenshot);
                        }
                        break;

                    case MIDDLE:
                    case UNREADY:
                        attempt = handleRetryStatus(identifier, status, attempt, isStartState);
                        break;

                    case FAILED:
                        attempt = handleFailureStatus(identifier, attempt);
                }
            }

            throw new AnalyzeException(String.format("Failed to classify image after reaching timeout for identifier: %s, md5 : %s", identifier, md5));

        } catch (IOException | InterruptedException | AnalyzeException e) {
            logger.error(String.format("Failed to analyze image classification status: %s", e.getMessage()), e);
            throw new AnalyzeException(e.getMessage(), screenshot, e);
        }

    }

    private boolean handleExistStatus(String identifier, ImageClassificationStatus status, boolean isStartState) throws AnalyzeException {
        String state = isStartState ? "start" : "end";

        if ( (isStartState && START == status) || (!isStartState && END == status) ) {
            logger.info(String.format("Image exist status classification found for identifier: %s, status: %s", identifier, status));
            return true;

        } else {
            throw new AnalyzeException(String.format("Failed to classify image exist status for identifier: %s, found status: %s, state: %s", identifier, status, state));
        }
    }

    private int handleRetryStatus(String identifier, ImageClassificationStatus status, int attempt, boolean isStartState) throws InterruptedException, AnalyzeException {
        String state = isStartState ? "start" : "end";
        int sleepInSeconds = status == UNREADY ? 8 : 2;
        logger.info(String.format("Failed to classify image after %s attempts for identifier: %s, status: %s, state: %s, retry again", attempt, identifier, status, state));
        TimeUnit.SECONDS.sleep(sleepInSeconds);

        return attempt+1;
    }

    private int handleFailureStatus(String identifier, int attempt) throws AnalyzeException, InterruptedException {
        if (attempt == 0) {
            logger.info(String.format("Failed to classify image after %s attempts for identifier: %s, retry again", attempt, identifier));
            TimeUnit.SECONDS.sleep(8);

            return attempt+1;

        } else {
            throw new AnalyzeException(String.format("Failed to classify failure statues image for identifier: %s", identifier));
        }
    }

    @Override
    public SpeedTestResult waitForFlashTestToFinish(String identifier, String finishIdentifier, List<Role> roles) throws InterruptedException, BrowserFailedException, AnalyzeException {
        String logPayload;
        long timeout = new Date().getTime() + TimeUnit.MINUTES.toMillis(4);
        try {
            do {
                TimeUnit.MILLISECONDS.sleep(800);
                executeRoles(roles);
                logPayload = Utils.readFile(logPath);

                if (System.currentTimeMillis() > timeout) {
                    throw new InterruptedException("Reached timeout, failed to find indicator: " + finishIdentifier + " in file: " + logPath);
                }

            } while (!logPayload.contains(finishIdentifier));

            return waitForImageAppearanceByImageClassification(identifier,  false);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new BrowserFailedException(e.getMessage(), e);
        }
    }

    private void executeRoles(List<Role> roles) {
        if (nonNull(roles)) {
            roles.forEach(this::executeRole);
        }
    }

    private void executeRole(Role role) {
        if (!role.isExecuted()) {
            By by = getByIdentifier(role.getIdentifier());

            try {
                WebElement webElement = webDriver.findElement(by);
                executeCommand(webElement, role.getCommand());

                if (role.isMono()) {
                    logger.info("Role: " + role + " has executed");
                    role.done();
                }

            } catch (NoSuchElementException e) {
                logger.info("Not Found the requested element");
            }
        }
    }

    private void executeCommand(WebElement webElement, Command command) {
        switch (command) {
            case CLICK:
                WebDriverWait wait = new WebDriverWait(webDriver, 30);
                wait.until(ExpectedConditions.elementToBeClickable(webElement));
                webElement.click();
                break;
        }
    }

    @Override
    public SpeedTestResult waitForTestToFinishByText(String identifier, SpeedTestNonFlashMetaData speedTestNonFlashMetaData) throws BrowserFailedException, InterruptedException {
        String result;
        int currentAttempt = 0;
        int numOfAttempts = waitForTestToFinishInSec / (int)TimeUnit.MILLISECONDS.toSeconds(waitForFinishIdentifier);

        do {
            checkBrowser();
            result = textAnalyzer.retrieveResultFromHtml(getHTMLPayload(), speedTestNonFlashMetaData.getFinishTextIdentifier(), speedTestNonFlashMetaData.getFinishIdentifierKbps(), 1);

            if (nonNull(result)) {
                return new SpeedTestResult(result, takeScreenSnapshot());
            }

            TimeUnit.MILLISECONDS.sleep(waitForFinishIdentifier);

        } while (++currentAttempt < numOfAttempts);

        throw new BrowserFailedException("Failure to find finish test identifier : " + identifier + " with text: " + speedTestNonFlashMetaData.getFinishTextIdentifier());
    }

    private By getByIdentifier(String identifier) {
        String[] identifiers = identifier.split("=");

        switch (identifiers[0]) {

            case "id" :
                return By.id(identifiers[1]);
            case "class":
                return By.className(identifiers[1]);
            case "cssSelector":
                return By.cssSelector(identifiers[1]);

            default:
                return By.id(identifiers[1]);
        }
    }

    private void checkBrowser() throws BrowserFailedException {
        try {
            webDriver.getTitle(); // Will throw UnreachableBrowserException if browser is closed

        } catch (UnreachableBrowserException | NoSuchSessionException e) {
            logger.error("Browser check error: " + e.getMessage(), e);
            throw new UserInterruptException(e.getMessage(), e);

        } catch (Exception e) {
            logger.error("Browser check error: " + e.getMessage(), e);
            throw new BrowserFailedException("Browser check error: " + e.getMessage(), e);
        }
    }

    private int getWaitForTestToFinishInSec(int waitForStartButton) {
        return waitForStartButton < 1000 ? 1 : (int) TimeUnit.MILLISECONDS.toSeconds(waitForStartButton);
    }

    private boolean waitForImageAppearanceByImageAnalyze(Set<WordIdentifier> textIdentifiers, int numOfAttempts , int waitForImageInSec, boolean clickImage, String findImageStatus) throws IOException, InterruptedException, BrowserFailedException, RequestFailureException {
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

        throw new BrowserFailedException("Failure to find " + findImageStatus + " test identifiers: " + textIdentifiers);
    }

    private boolean foundMatchingDescription(Set<WordIdentifier> textIdentifiers, boolean clickImage) throws IOException, AnalyzeException, OcrParsingException {
        Point point;
        OcrResponse ocrResponse = imageParsingService.parseImage(takeScreenSnapshot());
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

    private String getHTMLPayload() {
        JavascriptExecutor jse = (JavascriptExecutor)webDriver;
        String htmlPayload = String.valueOf(jse.executeScript(GET_HTML_JS));

        return htmlPayload;
    }

    public String getDriverPath() {
        if (isWindows()) {
            return "drivers/chromedriver.exe";
        } else {
            return "drivers/chromedriver_linux";
        }
    }
}