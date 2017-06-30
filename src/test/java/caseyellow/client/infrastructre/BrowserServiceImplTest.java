package caseyellow.client.infrastructre;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by Dan on 6/30/2017.
 */
@RunWith(Parameterized.class)
public class BrowserServiceImplTest {

    private String url;
    private String expectedTitle;
    private BrowserServiceImpl browserService;

    public BrowserServiceImplTest(String url, String expectedTitle) {
        this.url = url;
        this.expectedTitle = expectedTitle;
    }

    @Before
    public void setUp() throws Exception {
        browserService = new BrowserServiceImpl();
    }

    @Parameterized.Parameters
    public static List<String[]> dango() {
        String[][] arr = { {"http://speedtest.att.com/speedtest/", "AT&T High Speed Internet Speed Test"},
                           {"http://www.bezeq.co.il/internetandphone/internet/speedtest/", "בדיקת מהירות בזק -בזק"},
                           {"https://www.fast.com/", "Internet Speed Test | Fast.com"},
                           {"http://www.hot.net.il/heb/Internet/speed/", "בדיקת מהירות הוט - Speed Test"},
                           {"http://www.speedtest.net/", "Speedtest.net by Ookla - The Global Broadband Speed Test"},
                           {"http://www.speedof.me/", "SpeedOf.Me, HTML5 Speed Test | Non Flash/Java Broadband Speed Test"} };

        return Arrays.asList(arr);
    }

    @Test
    public void openBrowserTest() throws Exception {
        String actualTitle;
        WebDriver webDriver;
        Field webDriverField;

        browserService.openBrowser(url);
        webDriverField = browserService.getClass().getDeclaredField("webDriver");
        webDriverField.setAccessible(true);
        webDriver = (WebDriver) webDriverField.get(browserService);
        actualTitle = webDriver.getTitle();
        browserService.closeBrowser();

        assertEquals(expectedTitle, actualTitle);
    }

}