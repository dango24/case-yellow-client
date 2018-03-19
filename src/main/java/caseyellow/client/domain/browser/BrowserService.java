package caseyellow.client.domain.browser;

import caseyellow.client.domain.website.model.Role;
import caseyellow.client.domain.website.model.SpeedTestResult;
import caseyellow.client.exceptions.AnalyzeException;
import caseyellow.client.exceptions.BrowserFailedException;
import caseyellow.client.exceptions.UserInterruptException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url) throws IOException;
    void closeBrowser();
    void centralizedWebPage(int centralized) throws InterruptedException, BrowserFailedException;
    void pressFlashStartTestButton(String identifier) throws BrowserFailedException, UserInterruptException, AnalyzeException;
    void pressStartButtonById(String btnId) throws BrowserFailedException;
    SpeedTestResult waitForFlashTestToFinish(String identifier, String finishIdentifier, List<Role> roles) throws BrowserFailedException, UserInterruptException, InterruptedException, AnalyzeException;
    SpeedTestResult waitForTestToFinishByText(String identifier) throws BrowserFailedException, InterruptedException;
}
