package caseyellow.client.sevices.infrastrucre;

import caseyellow.client.exceptions.ConnectionException;
import caseyellow.client.exceptions.RequestFailureException;
import retrofit2.Call;

import java.io.IOException;
import java.util.Map;

public interface RequestHandler {
    <T extends Object> T execute(Call<T> request) throws RequestFailureException, ConnectionException;
    <T extends Object> Map<String, String> getResponseHeaders(Call<T> request) throws RequestFailureException, IOException;
}
