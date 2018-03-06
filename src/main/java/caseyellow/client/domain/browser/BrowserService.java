package caseyellow.client.domain.browser;

import caseyellow.client.domain.analyze.model.WordIdentifier;
import caseyellow.client.domain.website.model.Role;
import caseyellow.client.domain.website.model.SpeedTestNonFlashMetaData;
import caseyellow.client.domain.website.model.SpeedTestResult;
import caseyellow.client.exceptions.BrowserFailedException;
import caseyellow.client.exceptions.UserInterruptException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url) throws IOException;
    void closeBrowser();
    void centralizedWebPage(int centralized) throws InterruptedException, BrowserFailedException;
    void pressFlashStartTestButton(String identifier, Set<WordIdentifier> btnImageIdentifiers, int maxAttempts) throws BrowserFailedException, UserInterruptException, IOException, InterruptedException;
    void pressStartButtonById(String btnId) throws BrowserFailedException;
    SpeedTestResult waitForFlashTestToFinish(String identifier, String finishIdentifier, Set<WordIdentifier> identifiers, List<Role> roles) throws BrowserFailedException, UserInterruptException, InterruptedException;
    SpeedTestResult waitForTestToFinishByText(String identifier, SpeedTestNonFlashMetaData speedTestNonFlashMetaData) throws BrowserFailedException, InterruptedException;
}
