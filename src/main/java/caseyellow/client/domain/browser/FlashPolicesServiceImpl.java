package caseyellow.client.domain.browser;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.springframework.stereotype.Service;

@Service
public class FlashPolicesServiceImpl implements FlashPolicesService {

    private static final String CHROME_FLASH_SETTINGS = "chrome://settings/content/flash";

    private Logger logger = Logger.getLogger(FlashPolicesServiceImpl.class);

    @Override
    public void addPolicy(WebDriver driver, String url) {
        driver.get(CHROME_FLASH_SETTINGS);
        addSite(driver, url);
    }

    private void addSite(WebDriver driver, String site) {
        WebElement addSiteBox = getAddSiteBox(driver);
        WebElement inputSite = addSiteBox.findElement(getByIdentifier("id=site"));

        inputSite.sendKeys(site);
        addSiteBox.findElement(getByIdentifier("id=add")).click();
    }

    private WebElement getAddSiteBox(WebDriver driver) {
        System.out.println("Validate settings-ui header text");
        WebElement root1 = driver.findElement(By.tagName("settings-ui"));

        //Get shadow root element
        WebElement shadowRoot1 = expandRootElement(driver, root1);
        WebElement root2 = shadowRoot1.findElement(getByIdentifier("id=container"));
        WebElement main = root2.findElement(getByIdentifier("id=main"));

        WebElement shadowRoot3 = expandRootElement(driver, main);
        WebElement shadowRoot4 = shadowRoot3.findElement(getByIdentifier("class=showing-subpage"));
        WebElement shadowRoot5 = expandRootElement(driver, shadowRoot4);
        WebElement shadowRoot6 = shadowRoot5.findElement(getByIdentifier("id=advancedPage"));
        WebElement shadowRoot7 = shadowRoot6.findElement(By.tagName("settings-privacy-page"));
        WebElement shadowRoot8 = expandRootElement(driver, shadowRoot7);
        WebElement shadowRoot9 = shadowRoot8.findElement(getByIdentifier("id=pages"));
        WebElement shadowRoot10 = shadowRoot9.findElement(By.tagName("settings-subpage"));
        WebElement shadowRoot11 = shadowRoot10.findElement(By.tagName("category-setting-exceptions"));
        WebElement shadowRoot12 = expandRootElement(driver, shadowRoot11);
        WebElement shadowRoot13 = ((RemoteWebElement) shadowRoot12).findElementsByCssSelector("site-list").get(2); // Allow
        WebElement shadowRoot14 = expandRootElement(driver, shadowRoot13);
        WebElement shadowRoot15 = shadowRoot14.findElement(getByIdentifier("id=category"));
        ((RemoteWebElement) shadowRoot15).findElementById("addSite").click();

        WebElement shadowRoot16 = ((RemoteWebElement)shadowRoot14).findElementsByCssSelector("add-site-dialog").get(0); //
        WebElement shadowRoot17 = expandRootElement(driver, shadowRoot16);
        WebElement shadowRoot18 = shadowRoot17.findElement(getByIdentifier("id=dialog"));

        return shadowRoot18;
    }

    private By getByIdentifier(String identifier) {
        String[] identifiers = identifier.split("=");

        return identifiers[0].equals("id") ? By.id(identifiers[1]) :
                By.className(identifiers[1]);
    }

    private WebElement expandRootElement(WebDriver driver, WebElement element) {
        WebElement ele = (WebElement) ((JavascriptExecutor)driver).executeScript("return arguments[0].shadowRoot",element);
        return ele;
    }
}
