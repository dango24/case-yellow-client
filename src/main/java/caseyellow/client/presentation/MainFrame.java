package caseyellow.client.presentation;

import caseyellow.client.exceptions.LoginException;

import java.io.IOException;

public interface MainFrame {
    void login(String userName, String password) throws IOException, LoginException;
}
