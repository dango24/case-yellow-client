package caseyellow.client.sevices.googlevision;

import caseyellow.client.exceptions.OcrParsingException;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.sevices.gateway.services.GatewayService;
import caseyellow.client.sevices.googlevision.model.OcrResponse;
import caseyellow.client.domain.interfaces.OcrService;
import caseyellow.client.sevices.googlevision.model.GoogleVisionRequest;
import caseyellow.client.sevices.infrastrucre.RequestHandler;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class GoogleVisionService implements OcrService {

    @Value("${google_vision_url}")
    private String googleVisionUrl;

    private RequestHandler requestHandler;
    private GatewayService gatewayService;
    private GoogleVisionRetrofitRequests googleVisionRetrofitRequests;

    @Autowired
    public GoogleVisionService(RequestHandler requestHandler, GatewayService gatewayService) {
        this.requestHandler = requestHandler;
        this.gatewayService = gatewayService;
    }

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(googleVisionUrl)
                                           .build();

        googleVisionRetrofitRequests = retrofit.create(GoogleVisionRetrofitRequests.class);
    }

    @Override
    public void cancelRequest() {
        requestHandler.cancelRequest();
    }

    @Override
    public OcrResponse parseImage(String imgPath) throws IOException, OcrParsingException, RequestFailureException {

        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(imgPath);
        JsonNode response = requestHandler.execute(googleVisionRetrofitRequests.ocrRequest(gatewayService.googleVisionKey(), googleVisionRequest));
        OcrResponse ocrData = parseResponse(response);

        return ocrData;
    }

    private OcrResponse parseResponse(JsonNode response) throws IOException {
        JsonNode textAnnotations = response.at("/responses/0").get("textAnnotations");
        String wordsData = "{ \"textAnnotations\" : " + textAnnotations.toString() + "}";

        return new ObjectMapper().readValue(wordsData, OcrResponse.class);
    }
}
