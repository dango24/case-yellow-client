package caseyellow.client.sevices.gateway.services;

import caseyellow.client.exceptions.LoginException;
import caseyellow.client.sevices.gateway.model.AccountCredentials;

import java.io.IOException;

public interface GatewayService {
    String googleVisionKey();
    boolean login(AccountCredentials accountCredentials) throws IOException, LoginException;
}
