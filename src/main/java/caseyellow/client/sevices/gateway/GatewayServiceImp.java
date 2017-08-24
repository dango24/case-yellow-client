package caseyellow.client.sevices.gateway;

import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.sevices.infrastrucre.RequestHandler;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Primary
public class GatewayServiceImp implements DataAccessService {

    private DataAccessService service;

    @Value("${gateway_url}")
    private String gatewayUrl;

    private RequestHandler requestHandler;
    private GatewayRetrofitRequests gatewayRetrofitRequests;

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(gatewayUrl)
                                           .build();

        gatewayRetrofitRequests = retrofit.create(GatewayRetrofitRequests.class);
    }

    @Autowired
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Autowired
    @Qualifier("stubDataService")
    public void setService(DataAccessService service) {
        this.service = service;
    }

    @Override
    public void saveTest(Test test) throws RequestFailureException {
        try {
            String indented = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(test);
            requestHandler.execute(gatewayRetrofitRequests.printToShekerServer(indented));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int additionalTimeForWebTestToFinishInSec() {
        return service.additionalTimeForWebTestToFinishInSec();
    }

    @Override
    public SpeedTestWebSite getNextSpeedTestWebSite() {
        return service.getNextSpeedTestWebSite();
    }

    @Override
    public List<String> getNextUrls(int numOfComparisonPerTest) {
        return service.getNextUrls(numOfComparisonPerTest);
    }
}
