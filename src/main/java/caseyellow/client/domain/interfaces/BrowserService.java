package caseyellow.client.domain.interfaces;

import caseyellow.client.exceptions.BrowserCommandFailedException;
import caseyellow.client.exceptions.UserInterruptException;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url) throws IOException;
    void closeBrowser();
    void centralizedWebPage(String identifier);
    void pressFlashStartTestButton(String btnImagePath) throws BrowserCommandFailedException, UserInterruptException;
    void waitForFlashTestToFinish(String identifier) throws BrowserCommandFailedException, UserInterruptException;
    boolean waitForTestToFinishByText(String identifier, String finishTextIdentifier) throws BrowserCommandFailedException, InterruptedException;
    void pressStartButtonById(String btnId) throws BrowserCommandFailedException;
}
