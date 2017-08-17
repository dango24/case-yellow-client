package caseyellow.client.sevices.googlevision;

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
    @POST("v1/images:annotate")
    Call<JsonNode> ocrRequest(@Query("key")String key, @Body GoogleVisionRequest googleVisionRequest);
}
