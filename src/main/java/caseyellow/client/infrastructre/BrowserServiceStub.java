package caseyellow.client.infrastructre;

import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.exceptions.BrowserCommandFailedException;

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
    public void pressStartTestButton(String btnImagePath) throws BrowserCommandFailedException {

    }

    @Override
    public void waitForTestToFinish(String identifierPath) throws BrowserCommandFailedException {

    }

}
