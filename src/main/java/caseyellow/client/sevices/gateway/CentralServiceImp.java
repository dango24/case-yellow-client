package caseyellow.client.sevices.gateway;

import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.sevices.infrastrucre.RequestHandler;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

@Service
@Profile({"prod", "integration"})
public class CentralServiceImp implements DataAccessService {

    @Value("${central_url}")
    private String centralUrl;

    private RequestHandler requestHandler;
    private CentralRequests centralRequests;

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(centralUrl)
                .build();

        centralRequests = retrofit.create(CentralRequests.class);
    }

    @Autowired
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public void sendErrorMessage(String errorMessage) {
        requestHandler.execute(centralRequests.sendMessage(errorMessage));
    }

    @Override
    public void saveTest(Test test) throws RequestFailureException {
        UploadTest uploadTest;

        if (nonNull(test)) {
            uploadTest = createUploadTest(test);
            requestHandler.execute(centralRequests.upload(uploadTest.getPayload(), uploadTest.getParts()));
        }
    }

    @Override
    public int additionalTimeForWebTestToFinishInSec() {
        return requestHandler.execute(centralRequests.additionalTimeForWebTestToFinishInSec());
    }

    @Override
    public SpeedTestMetaData getNextSpeedTestWebSite() {
        return requestHandler.execute(centralRequests.getNextSpeedTestWebSite());
    }

    @Override
    public List<FileDownloadMetaData> getNextUrls(int numOfComparisonPerTest) {
        return requestHandler.execute(centralRequests.getNextUrls(numOfComparisonPerTest));
    }

    private UploadTest createUploadTest(Test test) {

        Map<Integer, String> snapshotMap =
            test.getComparisonInfoTests()
                .stream()
                .map(comparisonInfo -> comparisonInfo.getSpeedTestWebSite().getWebSiteDownloadInfoSnapshot())
                .collect(toMap(String::hashCode, Function.identity()));

        List<MultipartBody.Part> parts =
            snapshotMap.entrySet()
                       .stream()
                       .map(snapshot -> createRequestBodyPart(snapshot.getKey(), snapshot.getValue()))
                       .collect(Collectors.toList());

        RequestBody payload = RequestBody.create(MultipartBody.FORM, new Gson().toJson(test));

        return new UploadTest(payload, parts);
    }

    private MultipartBody.Part createRequestBodyPart(int key, String filePath) {
        File imgFile = new File(filePath);
        RequestBody imgRequestBody = RequestBody.create(MediaType.parse(".png"), imgFile);
        MultipartBody.Part imgPart = MultipartBody.Part.createFormData(String.valueOf(key), imgFile.getName(), imgRequestBody);

        return imgPart;
    }

    private static class UploadTest {

        private RequestBody payload;
        private List<MultipartBody.Part> parts;

        public UploadTest() {
        }

        public UploadTest(RequestBody payload, List<MultipartBody.Part> parts) {
            this.payload = payload;
            this.parts = parts;
        }

        public RequestBody getPayload() {
            return payload;
        }

        public void setPayload(RequestBody payload) {
            this.payload = payload;
        }

        public List<MultipartBody.Part> getParts() {
            return parts;
        }

        public void setParts(List<MultipartBody.Part> parts) {
            this.parts = parts;
        }
    }
}
