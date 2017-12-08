package caseyellow.client.sevices.gateway.services;

import caseyellow.client.exceptions.LoginException;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.gateway.model.ErrorMessage;
import caseyellow.client.sevices.infrastrucre.RequestHandler;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Service("gatewayService")
public class GatewayServiceImpl implements GatewayService {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String TOKEN_HEADER = "Authorization";

    @Value("${gateway_url}")
    private String gatewayUrl;

    private String token;
    private RequestHandler requestHandler;
    private GatewayRequests gatewayRequests;

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(gatewayUrl)
                                           .build();

        gatewayRequests = retrofit.create(GatewayRequests.class);
    }

    @Autowired
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public boolean login(AccountCredentials accountCredentials) throws IOException, LoginException {
        try {
            Map<String, String> headers = requestHandler.getResponseHeaders(gatewayRequests.login(accountCredentials));

            if (!headers.containsKey(TOKEN_HEADER)) {
                throw new LoginException("There is no authentication header at the login request");
            }

            token = headers.get(TOKEN_HEADER).replaceAll(TOKEN_PREFIX, "").trim();
            return true;

        } catch (RequestFailureException e) {
            handleError(e.getErrorCode(), e.getMessage());
            return false;
        }
    }


    private void handleError(int statusCode, String message) throws LoginException {
        try {
            switch (statusCode) {
                case 401:
                    ErrorMessage errorMessage = new ObjectMapper().readValue(message, ErrorMessage.class);
                    throw new LoginException(errorMessage.getError() + " " + errorMessage.getMessage());


                default:
                    throw new RequestFailureException(message, statusCode);
            }

        } catch (IOException e) {
            throw new RequestFailureException(message, statusCode);
        }
    }

}
