package caseyellow.client.sevices.googlevision;

import caseyellow.client.sevices.googlevision.model.GoogleVisionRequest;
import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoogleVisionRetrofitRequests {

   @Headers({
        "Accept: application/json",
        "Content-Type: application/json"
    })
    @POST("ocr_request")
    Call<JsonNode> ocrRequest(@Body GoogleVisionRequest googleVisionRequest);
}
