package caseyellow.client.sevices.infrastrucre;

import caseyellow.client.exceptions.ConnectionException;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.exceptions.UserInterruptException;
import okhttp3.Headers;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;
import java.util.function.Function;

import static caseyellow.client.domain.test.service.TestGeneratorImpl.TOKEN_EXPIRED_CODE;
import static java.util.stream.Collectors.toMap;

@Component
public class RequestHandlerImpl implements RequestHandler {

    private static final String TOKEN_EXPIRED = "tokenExpired";

    private Logger logger = Logger.getLogger(RequestHandlerImpl.class);


    @Override
    public <T extends Object> T execute(Call<T> request) throws RequestFailureException, ConnectionException {
        try {
            Response<T> response = request.execute();

            if (response.isSuccessful()) {
                return response.body();

            } else if (request.isCanceled() || Thread.currentThread().isInterrupted()) {
                logger.warn("User cancel request request");
                throw new UserInterruptException("User cancel request request");

            } else if (isTokenExpired(response)){
                logger.warn("Token expired");
                throw new RequestFailureException("Token expired", TOKEN_EXPIRED_CODE);

            } else {
                logger.error(String.format("Request Failed, error code: %s, error message: %s", response.code(), response.errorBody().string()));
                throw new RequestFailureException(response.errorBody().string(), response.code());
            }

        } catch (ConnectException e) {
            String errorMessage = String.format("Failed to produce connect to server, error: %s", e.getMessage());
            logger.error(errorMessage, e);
            throw new ConnectionException(errorMessage, e);

        } catch (IOException e) {
            if (e.getMessage().equals("Canceled")) {
                throw new UserInterruptException(e.getMessage(), e);
            } else {
                logger.error(String.format("Request Failed, error message: %s", e.getMessage()));
                throw new RequestFailureException(e.getMessage(), e);
            }
        }
    }

    private boolean isTokenExpired(Response response) {
        Map<String, String> headers = createHeadersMap(response.headers());

        return headers.containsKey(TOKEN_EXPIRED) && Boolean.valueOf(headers.get(TOKEN_EXPIRED));
    }

    @Override
    public <T> Map<String, String> getResponseHeaders(Call<T> request) throws RequestFailureException, IOException {
        Response<T> response = request.execute();

        if (response.isSuccessful()) {
            return createHeadersMap(response.headers());

        } else {
            throw new RequestFailureException(response.errorBody().string(), response.code());
        }
    }

    private Map<String,String> createHeadersMap(Headers headers) {

        return headers.names()
                      .stream()
                      .collect(toMap(Function.identity(), name -> headers.values(name).get(0)));
    }
}
