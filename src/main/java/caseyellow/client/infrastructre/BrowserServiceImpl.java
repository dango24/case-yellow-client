package caseyellow.client.infrastructre;

import caseyellow.client.common.Mapper;
import caseyellow.client.common.Utils;
import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.FindFailedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

import static caseyellow.client.common.Utils.*;
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

    @Value("${chromeDriverExecutorPath}")
    private String chromeDriverExecutorPath;

    private WebDriver webDriver;
    private Mapper mapper;
    private int additionTimeForWebTestToFinish;

    public BrowserServiceImpl() throws IOException {
        additionTimeForWebTestToFinish = 0;
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
    public void pressStartTestButton(String webSiteBtnIdentifier) throws FindFailedException {

        try {
            String imgLocation = getImgFromResources(btnDir + webSiteBtnIdentifier);

        } catch (Exception e) {
            throw new FindFailedException(e.getMessage());
        }
    }

    @Override
    public void waitForTestToFinish(String imgIdentifier) throws FindFailedException {

        try {
            String testFinishIdentifierImg = getImgFromResources(identifierDir + imgIdentifier);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FindFailedException(e.getMessage());
        }
    }

    private String imageInByteScreen(String imgPath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bufferedImage, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();

        StringJoiner sj = new StringJoiner(",");

        for (byte b : imageInByte) {
            sj.add(String.valueOf(b));
        }

        return sj.toString();
    }

    @Override
    public String takeScreenSnapshot() {
        File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
        return scrFile.getAbsolutePath();
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

        if (scrollDownPixel > 0) {
            scrollDown(toIntExact(scrollDownPixel));
        }
    }

    private void scrollDown(int scrollDownPixel) {
        JavascriptExecutor jse = (JavascriptExecutor)webDriver;
        jse.executeScript("window.scrollBy(0," + scrollDownPixel + ")", "");
    }
}
