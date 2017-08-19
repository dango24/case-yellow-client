package caseyellow.client.sevices.infrastrucre;

import caseyellow.client.exceptions.RequestFailureException;
import retrofit2.Call;

public interface RequestHandler {

    void cancelRequest();
    <T extends Object> T execute(Call<T> request) throws RequestFailureException;
}
