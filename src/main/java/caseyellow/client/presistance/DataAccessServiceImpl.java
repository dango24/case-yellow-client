package caseyellow.client.presistance;

import caseyellow.client.domain.model.test.Test;
import caseyellow.client.domain.model.website.SpeedTestWebSite;
import caseyellow.client.domain.services.SpeedTestWebSiteFactory;
import caseyellow.client.domain.services.interfaces.DataAccessService;
import caseyellow.client.exceptions.DataAccessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dango on 6/4/17.
 */
@Service
@Profile("production")
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
        String uri = buildURI(URIRequests.getSaveTestRequest());
        exchangePostRequest(uri, new ObjectMapper().valueToTree(test));
    }

    @Override
    public SpeedTestWebSite getNextSpeedTestWebSite() {
        String uri = buildURI(URIRequests.getNextSpeedTestWebSiteRequest());
        String response = exchangeGetRequest(uri, String.class);

        return speedTestWebSiteFactory.createSpeedTestWebSiteFromIdentifier(response);
    }

    @Override
    public List<String> getNextUrls(int numOfComparisonPerTest) {
        String uri = buildURI(URIRequests.getNextUrlsRequest()) + numOfComparisonPerTest;
        List<String> response = exchangeGetRequest(uri, List.class);

        return response;
    }

      private void exchangePostRequest(String uri, JsonNode body) {

        RequestEntity requestEntity;
        ResponseEntity<?> responseEntity;
        RestTemplate restTemplate = new RestTemplate();

        try {
            requestEntity = RequestEntity.post(new URI(uri))
                                         .contentType(MediaType.APPLICATION_JSON)
                                         .accept(MediaType.APPLICATION_JSON)
                                         .acceptCharset(Charset.forName("UTF-8"))
                                         .body(body);

            responseEntity = restTemplate.exchange(requestEntity, JsonNode.class);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                handleFailureRequest(responseEntity.getBody().toString(), responseEntity.getStatusCodeValue());
            }

        } catch (RestClientException | URISyntaxException e) {
            handleFailureRequest(e);
        } catch (Exception e) {
            handleFailureRequest(e);
        }
    }

    private <T extends Object> T exchangeGetRequest(String uri, Class<T> type) {

        ResponseEntity<?> response;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpHeaders = buildHttpHeaders();

        try {
            response = restTemplate.exchange(uri, HttpMethod.GET, httpHeaders, type);

            if (!response.getStatusCode().is2xxSuccessful()) {
                handleFailureRequest(response.getBody().toString(), response.getStatusCodeValue());;
            }

        } catch (RestClientException e) {
           throw new DataAccessException(e.getMessage());
        }

        return (T)response.getBody();
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

    private void handleFailureRequest(Exception e) {
        logger.error(e.getMessage(), e);

        throw new DataAccessException(e.getMessage());
    }

    private void handleFailureRequest(String bodyMessage, int statusCode) {
        String errorMessage = "Failed to retrieve request, status code: " + statusCode + " message:" + bodyMessage;
        logger.error(errorMessage);

        throw new DataAccessException(errorMessage);
    }

}
