package caseyellow.client.sevices.gateway;

import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface GatewayRetrofitRequests {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("save-test")
    Call<Void> saveTest(@Body Test test);

    @POST("rc9s21rc")
    Call<Void> printToShekerServer(@Body String message);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("additional-time")
    Call<Integer> additionalTimeForWebTestToFinishInSec();

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("next-test-website")
    SpeedTestWebSite getNextSpeedTestWebSite();

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("next-urls")
    List<String> getNextUrls(@Query("num_of_comparison_per_test") int numOfComparisonPerTest);
}
