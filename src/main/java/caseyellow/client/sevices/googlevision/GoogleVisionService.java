package caseyellow.client.sevices.googlevision;

import caseyellow.client.exceptions.OcrParsingException;
import caseyellow.client.infrastructre.image.recognition.OcrResponse;
import caseyellow.client.domain.interfaces.OcrService;
import caseyellow.client.sevices.googlevision.model.GoogleVisionRequest;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.SocketTimeoutException;

@Service
public class GoogleVisionService implements OcrService {

    @Value("${google_vision_url}")
    private String googleVisionUrl;

    @Value("${google_vision_key}")
    private String googleVisionKey;

    private GoogleVisionRetrofitRequests googleVisionRetrofitRequests;

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(googleVisionUrl)
                                           .build();

        googleVisionRetrofitRequests = retrofit.create(GoogleVisionRetrofitRequests.class);
    }

    @Override
    public OcrResponse parseImage(String imgPath) throws IOException, OcrParsingException {
        try {
            GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(imgPath);
            Response<JsonNode> response = googleVisionRetrofitRequests.ocrRequest(googleVisionKey, googleVisionRequest).execute();
            JsonNode responseStr = response.body().at("/responses/0").get("textAnnotations");
            String wordsData = "{ \"textAnnotations\" : " + responseStr.toString() + "}";
            OcrResponse ocrData = new ObjectMapper().readValue(wordsData, OcrResponse.class);

            return ocrData;

        } catch (SocketTimeoutException e) {
            throw e;
        } catch(Exception e) {
            throw new OcrParsingException(e.getMessage(), e);
        }
    }
}
