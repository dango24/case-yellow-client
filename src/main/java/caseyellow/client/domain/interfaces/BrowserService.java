package caseyellow.client.domain.interfaces;

import caseyellow.client.exceptions.FindFailedException;

import java.io.Closeable;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url);
    void closeBrowser();
    void centralizedWebPage(String identifier);
    void addAdditionalTimeForWebTestToFinish(int additionTimeInSec);
    void pressStartTestButton(String btnImagePath) throws FindFailedException;
    void waitForTestToFinish(String identifierPath) throws FindFailedException;
    String takeScreenSnapshot();
    String getBrowserName();

}
