package caseyellow.client.infrastructre;

import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.FindFailedException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by dango on 7/25/17.
 */
//@Service
//@Primary
public class BrowserServiceStub implements BrowserService {

    @Override
    public void openBrowser(String url) throws IOException {

    }

    @Override
    public void closeBrowser() {

    }

    @Override
    public void centralizedWebPage(String identifier) {

    }

    @Override
    public void addAdditionalTimeForWebTestToFinish(int additionTimeInSec) {

    }

    @Override
    public void pressStartTestButton(String btnImagePath) throws FindFailedException {

    }

    @Override
    public void waitForTestToFinish(String identifierPath) throws FindFailedException {

    }

    @Override
    public String takeScreenSnapshot() {
        return null;
    }

    @Override
    public String getBrowserName() {
        return null;
    }
}
