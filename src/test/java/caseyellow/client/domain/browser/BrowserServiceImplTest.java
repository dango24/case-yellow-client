package caseyellow.client.domain.browser;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static caseyellow.client.common.Utils.getTempFileFromResources;
import static org.junit.Assert.*;

public class BrowserServiceImplTest {

    private WebDriver driver;
    private String driverPath = "F:/Jars/chromedriver/";

    @Before
    public void setUp() throws IOException {
        String chromeDriver = getTempFileFromResources("drivers/chromedriver.exe").getAbsolutePath();
        System.setProperty("webdriver.chrome.driver", chromeDriver);
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    private void setNormally() {
        this.driver = new ChromeDriver();
        System.out.println("Open Chrome downloads");
        driver.get("chrome://settings/content/flash");
    }

    private void setWithOptions() throws IOException {
        setUp();
        Map<String, Object> prefs = new HashMap<>();
//        prefs.put("profile.default_content_setting_values.plugins", 1);
//        prefs.put("profile.content_settings.plugin_whitelist.adobe-flash-player", 1);
//        prefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);
        prefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);
//        prefs.put("PluginsAllowedForUrls", "http://www.hot.net.il");

//        prefs.put("credentials_enable_service", false);
//        prefs.put("profile.password_manager_enabled", false);

        ChromeOptions options = new ChromeOptions();
//        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//        options.addArguments("start-maximized");
//        options.setExperimentalOption("excludeSwitches", Arrays.asList("disable-component-update"));
        options.setExperimentalOption("prefs", prefs);
        this.driver = new ChromeDriver(options);
    }

    @Ignore
    @Test
    public void testWithChromeOptions() throws Exception{
        setWithOptions();
        driver.get("http://www.hot.net.il/heb/Internet/speed/");

        assertTrue(true);
    }

    @Ignore
    @Test
    public void testGetText_FromShadowDOMElements() throws IOException {
        driver.get("chrome://settings/content/flash");

        System.out.println("Validate settings-ui header text");
        WebElement root1 = driver.findElement(By.tagName("settings-ui"));

        //Get shadow root element
        WebElement shadowRoot1 = expandRootElement(root1);
        WebElement root2 = shadowRoot1.findElement(getByIdentifier("id=container"));
        WebElement main = root2.findElement(getByIdentifier("id=main"));

        WebElement shadowRoot3 = expandRootElement(main);
        WebElement shadowRoot4 = shadowRoot3.findElement(getByIdentifier("class=showing-subpage"));

        WebElement shadowRoot5 = expandRootElement(shadowRoot4);
        WebElement shadowRoot6 = shadowRoot5.findElement(getByIdentifier("id=advancedPage"));
        WebElement shadowRoot7 = shadowRoot6.findElement(By.tagName("settings-privacy-page"));
        WebElement shadowRoot8 = expandRootElement(shadowRoot7);
        WebElement shadowRoot9 = shadowRoot8.findElement(getByIdentifier("id=pages"));
        WebElement shadowRoot10 = shadowRoot9.findElement(By.tagName("settings-subpage"));
        WebElement shadowRoot11 = shadowRoot10.findElement(By.tagName("category-setting-exceptions"));
        WebElement shadowRoot12 = expandRootElement(shadowRoot11);
        WebElement shadowRoot13 = ((RemoteWebElement) shadowRoot12).findElementsByCssSelector("site-list").get(2); // Allow
        WebElement shadowRoot14 = expandRootElement(shadowRoot13);
        WebElement shadowRoot15 = shadowRoot14.findElement(getByIdentifier("id=category"));
        ((RemoteWebElement) shadowRoot15).findElementById("addSite").click();

        WebElement shadowRoot16 = ((RemoteWebElement)shadowRoot14).findElementsByCssSelector("add-site-dialog").get(0); //
        WebElement shadowRoot17 = expandRootElement((shadowRoot16));
        WebElement shadowRoot18 = shadowRoot17.findElement(getByIdentifier("id=dialog"));


        WebElement shadowRoot19 = ((RemoteWebElement) shadowRoot18).findElementById("site");


        // Verify header title
        assertEquals("Downloads", "dango");

    }

    private WebElement getAddSiteBox() {
        System.out.println("Validate settings-ui header text");
        WebElement root1 = driver.findElement(By.tagName("settings-ui"));

        //Get shadow root element
        WebElement shadowRoot1 = expandRootElement(root1);
        WebElement root2 = shadowRoot1.findElement(getByIdentifier("id=container"));
        WebElement main = root2.findElement(getByIdentifier("id=main"));

        WebElement shadowRoot3 = expandRootElement(main);
        WebElement shadowRoot4 = shadowRoot3.findElement(getByIdentifier("class=showing-subpage"));

        WebElement shadowRoot5 = expandRootElement(shadowRoot4);
        WebElement shadowRoot6 = shadowRoot5.findElement(getByIdentifier("id=advancedPage"));
        WebElement shadowRoot7 = shadowRoot6.findElement(By.tagName("settings-privacy-page"));
        WebElement shadowRoot8 = expandRootElement(shadowRoot7);
        WebElement shadowRoot9 = shadowRoot8.findElement(getByIdentifier("id=pages"));
        WebElement shadowRoot10 = shadowRoot9.findElement(By.tagName("settings-subpage"));
        WebElement shadowRoot11 = shadowRoot10.findElement(By.tagName("category-setting-exceptions"));
        WebElement shadowRoot12 = expandRootElement(shadowRoot11);
        WebElement shadowRoot13 = ((RemoteWebElement) shadowRoot12).findElementsByCssSelector("site-list").get(2); // Allow
        WebElement shadowRoot14 = expandRootElement(shadowRoot13);
        WebElement shadowRoot15 = shadowRoot14.findElement(getByIdentifier("id=category"));
        ((RemoteWebElement) shadowRoot15).findElementById("addSite").click();

        WebElement shadowRoot16 = ((RemoteWebElement)shadowRoot14).findElementsByCssSelector("add-site-dialog").get(0); //
        WebElement shadowRoot17 = expandRootElement((shadowRoot16));
        WebElement shadowRoot18 = shadowRoot17.findElement(getByIdentifier("id=dialog"));

        return shadowRoot18;
    }

    @Ignore
    @Test
    public void testInput() {
        setNormally();
        addSite("Dango");
        addSite("Oren");
        System.out.println("dango");
    }

    private void addSite(String site) {
        WebElement addSiteBox = getAddSiteBox();
        WebElement inputSite = addSiteBox.findElement(getByIdentifier("id=site"));

        inputSite.sendKeys(site);
        addSiteBox.findElement(getByIdentifier("id=add")).click();
    }

    //Returns webelement
    public WebElement expandRootElement(WebElement element) {
        WebElement ele = (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot",element);
        return ele;
    }

    private By getByIdentifier(String identifier) {
        String[] identifiers = identifier.split("=");

        return identifiers[0].equals("id") ? By.id(identifiers[1]) :
                By.className(identifiers[1]);
    }
}