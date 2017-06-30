package caseyellow.client.presistance;

import caseyellow.client.domain.model.test.Test;
import caseyellow.client.domain.model.website.SpeedTestWebSite;
import caseyellow.client.domain.services.SpeedTestWebSiteFactory;
import caseyellow.client.domain.services.interfaces.DataAccessService;
import caseyellow.client.exceptions.DataAccessException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dango on 6/4/17.
 */
@Service
public class DataAccessServiceImpl implements DataAccessService {

    private Logger logger = Logger.getLogger(DataAccessServiceImpl.class);

    private final String REQUEST_URI_SCHEMA = "%s://%s:%s/%s";

    private ConnectionConfig connectionConfig;
    private URIRequests URIRequests;
    private SpeedTestWebSiteFactory speedTestWebSiteFactory;

    @Autowired
    public DataAccessServiceImpl(SpeedTestWebSiteFactory speedTestWebSiteFactory,
                                 URIRequests URIRequests,
                                 ConnectionConfig connectionConfig) {

        this.speedTestWebSiteFactory = speedTestWebSiteFactory;
        this.connectionConfig = connectionConfig;
        this.URIRequests = URIRequests;
    }

    @Override
    public void saveTest(Test test) {
        System.out.println("save test " + this.getClass().getName());
    }

    @Override
    public SpeedTestWebSite getNextSpeedTestWebSite() {
        String uri = buildURI(URIRequests.getNextSpeedTestWebSiteRequest());
        String response = exchange(uri, String.class);

        return speedTestWebSiteFactory.createSpeedTestWebSiteFromIdentifier(response);
    }

    @Override
    public List<String> getNextUrls(int numOfComparisonPerTest) {
        String uri = buildURI(URIRequests.getNextUrlsRequest()) + numOfComparisonPerTest;
        List<String> response = exchange(uri, List.class);

        return response;
    }

    private <T extends Object> T exchange(String uri, Class<T> type) {

        ResponseEntity<?> responseEntity;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpHeaders = buildHttpHeaders();

        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpHeaders, type);

        } catch (RestClientException e) {
           throw new DataAccessException(e.getMessage());
        }

        return (T)responseEntity.getBody();
    }

    private HttpEntity<String> buildHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));

        return new HttpEntity<>(httpHeaders);
    }

    private String buildURI(String urlQuery) {
        String httpProtocol = connectionConfig.isSecure() ? "https" : "http";

        return String.format(REQUEST_URI_SCHEMA,
                             httpProtocol,
                             connectionConfig.getHost(),
                             connectionConfig.getPort(),
                             urlQuery);
    }
}
