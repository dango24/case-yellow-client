package caseyellow.client.sevices.gateway;

import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.sevices.infrastrucre.RequestHandler;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.util.List;

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
        requestHandler.execute(centralRequests.saveTest(test));
    }

    @Override
    public int additionalTimeForWebTestToFinishInSec() {
        return requestHandler.execute(centralRequests.additionalTimeForWebTestToFinishInSec());
    }

    @Override
    public SpeedTestWebSite getNextSpeedTestWebSite() {
        return requestHandler.execute(centralRequests.getNextSpeedTestWebSite());
    }

    @Override
    public List<FileDownloadMetaData> getNextUrls(int numOfComparisonPerTest) {
        return requestHandler.execute(centralRequests.getNextUrls(numOfComparisonPerTest));
    }
}
