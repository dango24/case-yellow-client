package caseyellow.client.sevices.infrastrucre;

import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.exceptions.UserInterruptException;
import okhttp3.Headers;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static caseyellow.client.domain.test.service.TestGenerator.TOKEN_EXPIRED_CODE;
import static java.util.stream.Collectors.toMap;

@Component
public class RequestHandlerImpl implements RequestHandler {

    private static final int INTERNAL_ERROR_CODE = 420;
    private static final String TOKEN_EXPIRED = "tokenExpired";

    private Logger logger = Logger.getLogger(RequestHandlerImpl.class);

    private Call<? extends Object> currentRequest = null;

    @Override
    public void cancelRequest() {
        try {
            if (Objects.nonNull(currentRequest)) {
                currentRequest.cancel();
            }
        } catch (Exception e) {
            logger.error("Failed to cancel request, " + e.getMessage(), e);
        }
    }

    @Override
    public <T extends Object> T execute(Call<T> request) throws RequestFailureException {
        try {
            currentRequest = request;
            return executeRequest();

        } catch (IOException e) {
            if (e.getMessage().equals("Canceled")) {
                throw new UserInterruptException(e.getMessage(), e);
            } else {
                throw new RequestFailureException(e.getMessage(), e);
            }

        } finally {
            currentRequest = null;
        }
    }

    private <T extends Object> T executeRequest() throws IOException, RequestFailureException {
        Response<T> response = (Response<T>) currentRequest.execute();

        if (response.isSuccessful()) {
            return response.body();

        } else if (currentRequest.isCanceled()) {
            throw new UserInterruptException("User cancelRequest request");
        } else if (isTokenExpired(response)){
            throw new RequestFailureException("Token expired", TOKEN_EXPIRED_CODE);
        } else {
            throw new RequestFailureException(response.errorBody().string(), response.code());
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
