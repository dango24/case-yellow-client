package caseyellow.client.domain.browser;

import caseyellow.client.App;
import caseyellow.client.domain.analyze.model.WordIdentifier;
import caseyellow.client.domain.website.model.Command;
import caseyellow.client.domain.website.model.Role;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@ActiveProfiles("prod")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class BrowserServiceImplTest {

    @Autowired
    private BrowserService browserService;

    @After
    public void tearDown() throws Exception {
        browserService.closeBrowser();
    }

    @Test
    public void removeBezeq() throws Exception {
        browserService.openBrowser("http://www.bezeq.co.il/internetandphone/internet/speedtest/");
        //data-role
        WordIdentifier wordIdentifier = new WordIdentifier("בדוק", 1);
        WordIdentifier wordIdentifier2 = new WordIdentifier("שוב", 1);

        Set<WordIdentifier> wordIdentifiers = new HashSet<>();
        wordIdentifiers.add(wordIdentifier);
        wordIdentifiers.add(wordIdentifier2);
        Role role = new Role("cssSelector=img[data-role='close-button']", Command.CLICK, true);
        browserService.waitForFlashTestToFinish("SpeedTetsAddDiagnostic", wordIdentifiers, Arrays.asList(role));
    }

    private By getByIdentifier(String identifier) {
        String[] identifiers = identifier.split("=");

        return identifiers[0].equals("id") ? By.id(identifiers[1]) :
                By.className(identifiers[1]);
    }
}