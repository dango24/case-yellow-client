package caseyellow.client.sevices.gateway.services;

import caseyellow.client.domain.test.model.ConnectionDetails;
import caseyellow.client.exceptions.LoginException;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.gateway.model.LoginDetails;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GatewayService {
    LoginDetails login(AccountCredentials accountCredentials) throws IOException, LoginException;
    Map<String, List<String>> getConnectionDetails();
    void saveConnectionDetails(ConnectionDetails connectionDetails);
}
