package caseyellow.client.presentation;

import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.exceptions.LoginException;

import java.io.IOException;

public interface MainFrame extends MessagesService {
    void testDone();
    void testStopped();
    void formInitView();
    void disableApp(boolean tokenExpired);
    void login(String userName, String password) throws IOException, LoginException, InterruptedException;
    void saveConnectionDetails(String infrastructure, String speed);
}
