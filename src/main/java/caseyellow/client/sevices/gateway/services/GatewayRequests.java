package caseyellow.client.sevices.gateway.services;

import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.gateway.model.GoogleVisionKey;
import caseyellow.client.sevices.gateway.model.PreSignedUrl;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface GatewayRequests {

    @Headers({
        "Content-Type: application/json"
    })
    @POST("login")
    Call<Void> login(@Body AccountCredentials accountCredentials);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/additional-time")
    Call<Integer> additionalTimeForWebTestToFinishInSec(@HeaderMap Map<String, String> headers);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/next-web-site")
    Call<SpeedTestMetaData> getNextSpeedTestWebSite(@HeaderMap Map<String, String> headers);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/next-urls")
    Call<List<FileDownloadMetaData>> getNextUrls(@HeaderMap Map<String, String> headers ,
                                                 @Query("num_of_comparison_per_test") int numOfComparisonPerTest);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/pre-signed-url")
    Call<PreSignedUrl> generatePreSignedUrl(@HeaderMap Map<String, String> headers,
                                            @Query("user_ip") String userIP,
                                            @Query("file_name") String fileName);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("central/save-test")
    Call<Void> saveTest(@HeaderMap Map<String, String> headers, @Body Test test);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/google-vision-key")
    Call<GoogleVisionKey> googleVisionKey(@HeaderMap Map<String, String> headers);
}
