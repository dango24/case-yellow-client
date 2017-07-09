package caseyellow.client.infrastructre;

import caseyellow.client.common.Mapper;
import caseyellow.client.common.Utils;
import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.FindFailedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.sikuli.script.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static caseyellow.client.common.Utils.getImgFromResources;
import static java.lang.Math.toIntExact;

/**
 * Created by Dan on 6/30/2017.
 */
@Component
public class BrowserServiceImpl implements BrowserService {

    @Value("${waitForTestToFinishInSec}")
    private final int waitForTestToFinishInSec = 120;
    private Logger logger = Logger.getLogger(BrowserServiceImpl.class);

    @Value("${buttons-dir}")
    private String btnDir;

    @Value("${identifier-dir}")
    private String identifierDir;

    private WebDriver webDriver;
    private Mapper mapper;
    private int additionTimeForWebTestToFinish;

    public BrowserServiceImpl() {
        additionTimeForWebTestToFinish = 0;
        initWebDriver();
    }

    private void initWebDriver() {
        String chromeDriver = "C:\\Users\\Dan\\IdeaProjects\\case-yellow-client\\src\\main\\resources\\drivers\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriver);
        this.webDriver = new ChromeDriver();
    }

    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void openBrowser(String url) {

        try {
            webDriver.get(url);

        } catch (Exception e) {
            logger.error("Failed to open browser, reattempt again with new driver");
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
    public void pressStartTestButton(String webSiteBtnIdentifier) throws FindFailedException {

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

    @Override
    public void addAdditionalTimeForWebTestToFinish(int additionTimeInSec) {
        this.additionTimeForWebTestToFinish = additionTimeInSec;
    }

    @Override
    public void centralizedWebPage(String identifier) {
        String screenResolution = Utils.getScreenResolution();
        long scrollDownPixel = mapper.getPixelScrollDown(identifier, screenResolution);

        scrollDown(toIntExact(scrollDownPixel));
    }

    private void scrollDown(int scrollDownPixel) {
        JavascriptExecutor jse = (JavascriptExecutor)webDriver;
        jse.executeScript("window.scrollBy(0," + scrollDownPixel + ")", "");
    }

}
