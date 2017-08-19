package caseyellow.client.domain.browser;

import caseyellow.client.domain.analyze.model.WordIdentifier;
import caseyellow.client.exceptions.BrowserFailedException;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.exceptions.UserInterruptException;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Dan on 6/30/2017.
 */
public interface BrowserService {
    void openBrowser(String url) throws IOException;
    void closeBrowser();
    void centralizedWebPage(int centralized) throws InterruptedException, BrowserFailedException;
    void pressFlashStartTestButton(Set<WordIdentifier> btnImageIdentifiers) throws BrowserFailedException, UserInterruptException, IOException, InterruptedException;
    void waitForFlashTestToFinish(Set<WordIdentifier> identifiers) throws BrowserFailedException, UserInterruptException;
    boolean waitForTestToFinishByText(String identifier, String finishTextIdentifier) throws BrowserFailedException, InterruptedException;
    void pressStartButtonById(String btnId) throws BrowserFailedException;
}
