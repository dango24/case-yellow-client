package caseyellow.client.sevices.gateway.services;

import caseyellow.client.domain.analyze.model.*;
import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.test.model.ConnectionDetails;
import caseyellow.client.domain.test.model.FailedTestDetails;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.gateway.model.HTMLParserRequest;
import caseyellow.client.sevices.gateway.model.PreSignedUrl;
import caseyellow.client.sevices.gateway.model.StartTestDetails;
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
    Call<List<FileDownloadProperties>> getNextUrls(@HeaderMap Map<String, String> headers);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/pre-signed-url")
    Call<PreSignedUrl> generatePreSignedUrl(@HeaderMap Map<String, String> headers,
                                            @Query("file_key") String fileKey);

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
    @POST("central/start-test")
    Call<Void> startTest(@HeaderMap Map<String, String> headers, @Body StartTestDetails startTestDetails);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("central/failed-test")
    Call<Void> failedTest(@HeaderMap Map<String, String> headers, @Body FailedTestDetails failedTestDetails);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/connection-details")
    Call<Map<String, List<String>>> getConnectionDetails(@HeaderMap Map<String, String> headers);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("central/save-connection-details")
    Call<Void> saveConnectionDetails(@HeaderMap Map<String, String> headers, @Body ConnectionDetails connectionDetails);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("classify-image")
    Call<ImageClassificationResult> classifyImage(@HeaderMap Map<String, String> headers,
                                                  @Query("identifier") String identifier,
                                                  @Body VisionRequest visionRequest);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("is-description-exist")
    Call<DescriptionMatch> isDescriptionExist(@HeaderMap Map<String, String> headers,
                                              @Query("identifier")String identifier,
                                              @Query("startTest")boolean startTest,
                                              @Body GoogleVisionRequest visionRequest);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("parse-html")
    Call<HTMLParserResult> retrieveResultFromHtml(@HeaderMap Map<String, String> headers,
                                                  @Query("identifier")String identifier,
                                                  @Body HTMLParserRequest htmlParserRequest);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("start-button-successfully-found")
    Call<Void> startButtonSuccessfullyFound(@HeaderMap Map<String, String> tokenHeader,
                                            @Query("identifier")String identifier,
                                            @Query("x")int x,
                                            @Query("y")int y,
                                            @Body VisionRequest visionRequest);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("start-button-failed")
    Call<Void> startButtonFailed(@HeaderMap Map<String, String> tokenHeader,
                                 @Query("identifier")String identifier,
                                 @Query("x")int x,
                                 @Query("y")int y,
                                 @Body VisionRequest visionRequest);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/chrome-options-arguments")
    Call<List<String>> getChromeOptionsArguments(@HeaderMap Map<String, String> headers);

}
