package caseyellow.client.sevices.gateway.services;

import caseyellow.client.exceptions.LoginException;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.infrastrucre.RequestHandler;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
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
    public String login(AccountCredentials accountCredentials) throws IOException, LoginException {
        Map<String, String> headers = requestHandler.getResponseHeaders(gatewayRequests.login(accountCredentials));

        if (!headers.containsKey(TOKEN_HEADER)) {
            throw new LoginException("There is no authentication header at the login request");
        }

        return headers.get(TOKEN_HEADER).replaceAll(TOKEN_PREFIX, "");
    }
}
