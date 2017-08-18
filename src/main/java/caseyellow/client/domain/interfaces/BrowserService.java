package caseyellow.client.domain.interfaces;

import caseyellow.client.domain.analyze.model.WordIdentifier;
import caseyellow.client.exceptions.BrowserCommandFailedException;
import caseyellow.client.exceptions.UserInterruptException;

import java.awt.*;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url) throws IOException;
    void closeBrowser();
    void centralizedWebPage(int centralized) throws InterruptedException;
    void pressFlashStartTestButton(Set<WordIdentifier> btnImageIdentifiers) throws BrowserCommandFailedException, UserInterruptException, IOException, InterruptedException;
    void waitForFlashTestToFinish(Set<WordIdentifier> identifiers) throws BrowserCommandFailedException, UserInterruptException;
    boolean waitForTestToFinishByText(String identifier, String finishTextIdentifier) throws BrowserCommandFailedException, InterruptedException;
    void pressStartButtonById(String btnId) throws BrowserCommandFailedException;
}
