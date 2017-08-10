package caseyellow.client.domain.interfaces;

import caseyellow.client.exceptions.BrowserCommandFailedException;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url) throws IOException;
    void closeBrowser();
    void centralizedWebPage(String identifier);
    void pressStartTestButton(String btnImagePath) throws BrowserCommandFailedException;
    void waitForTestToFinish(String identifierPath) throws BrowserCommandFailedException;
}
