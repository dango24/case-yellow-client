package caseyellow.client.sevices.gateway;

import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface GatewayRetrofitRequests {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("rc9s21rc")
    Call<Void> saveTest(@Body Test test);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("rc9s21rc")
    Call<Void> sendMessage(@Body String message);

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
    SpeedTestMetaData getNextSpeedTestWebSite();

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("next-urls")
    List<String> getNextUrls(@Query("num_of_comparison_per_test") int numOfComparisonPerTest);
}
