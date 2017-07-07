package caseyellow.client.domain.interfaces;

import caseyellow.client.exceptions.FindFailedException;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url);
    void closeBrowser();
    void addAdditionalTimeForWebTestToFinish(int additionTimeInSec);
    void pressTestButton(String btnImagePath) throws FindFailedException;
    void waitForTestToFinish(String identifierPath) throws FindFailedException;
    String takeScreenSnapshot();
    String getBrowserName();
}
