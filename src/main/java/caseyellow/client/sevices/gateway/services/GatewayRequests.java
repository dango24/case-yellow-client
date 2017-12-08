package caseyellow.client.sevices.gateway.services;

import caseyellow.client.sevices.gateway.model.AccountCredentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GatewayRequests {

    @Headers({
        "Content-Type: application/json"
    })
    @POST("login")
    Call<Void> login(@Body AccountCredentials accountCredentials);
}
