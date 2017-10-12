package caseyellow.client.sevices.central;

import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface CentralRequests {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("save-test")
    Call<Void> saveTest(@Body Test test);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("send-message")
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
    @GET("next-web-site")
    Call<SpeedTestMetaData> getNextSpeedTestWebSite();

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("next-urls")
    Call<List<FileDownloadMetaData>> getNextUrls(@Query("num_of_comparison_per_test") int numOfComparisonPerTest);

    @Multipart
    @POST("save-test")
    Call<Void> upload(
            @Part("payload") RequestBody message,
            @Part List<MultipartBody.Part> files
    );
}
