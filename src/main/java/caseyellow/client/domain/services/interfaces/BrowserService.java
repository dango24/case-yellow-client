package caseyellow.client.domain.services.interfaces;

import org.sikuli.script.FindFailed;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url);
    void closeBrowser();
    void pressTestButton(String btnImagePath) throws FindFailed;
    void waitForTestToFinish(String identifierPath, int waitForTestToFinishInSec) throws FindFailed;
    String takeScreenSnapshot();
    String getBrowserName();
}
