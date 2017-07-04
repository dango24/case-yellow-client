package caseyellow.client.domain.interfaces;

import caseyellow.client.exceptions.FindFailedException;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url);
    void closeBrowser();
    void pressTestButton(String btnImagePath) throws FindFailedException;
    void waitForTestToFinish(String identifierPath, int waitForTestToFinishInSec) throws FindFailedException;
    String takeScreenSnapshot();
    String getBrowserName();
}
