package caseyellow.client.sevices.gateway.services;

import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.test.model.Test;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.LoginException;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.gateway.model.ErrorMessage;
import caseyellow.client.sevices.gateway.model.PreSignedUrl;
import caseyellow.client.sevices.infrastrucre.RequestHandler;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Profile("prod")
@Service("gatewayService")
public class GatewayServiceImpl implements GatewayService, DataAccessService {

    private Logger logger = Logger.getLogger(GatewayServiceImpl.class);

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String TOKEN_HEADER = "Authorization";

    @Value("${gateway_url}")
    private String gatewayUrl;

    private String token;
    private RequestHandler requestHandler;
    private GatewayRequests gatewayRequests;

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(gatewayUrl).build();
        gatewayRequests = retrofit.create(GatewayRequests.class);
    }

    @Autowired
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public boolean login(AccountCredentials accountCredentials) throws IOException, LoginException {
        try {
            Map<String, String> headers = requestHandler.getResponseHeaders(gatewayRequests.login(accountCredentials));

            if (!headers.containsKey(TOKEN_HEADER)) {
                throw new LoginException("There is no authentication header at the login request");
            }

            token = headers.get(TOKEN_HEADER)
                           .replaceAll(TOKEN_PREFIX, "")
                           .trim();

            return true;

        } catch (RequestFailureException e) {
            handleError(e.getErrorCode(), e.getMessage());
            return false;
        }
    }

    @Override
    public String googleVisionKey() {
        return requestHandler.execute(gatewayRequests.googleVisionKey(createTokenHeader())).getGoogleVisionKey();
    }

    @Override
    public void sendErrorMessage(String errorMessage) {
//        requestHandler.execute(centralRequests.sendMessage(errorMessage));
    }

    @Override
    public void saveTest(Test test) throws RequestFailureException {
        uploadSnapshotImages(test);
        requestHandler.execute(gatewayRequests.saveTest(createTokenHeader(), test));
    }

    private void uploadSnapshotImages(Test test) {
        Map<Integer, String> snapshotMap =
                test.getComparisonInfoTests()
                    .stream()
                    .map(ComparisonInfo::getSpeedTestWebSite)
                    .collect(toMap(SpeedTestWebSite::getKey, SpeedTestWebSite::getWebSiteDownloadInfoSnapshot));

        Map<Integer, PreSignedUrl> preSignedUrls =
                snapshotMap.keySet()
                           .stream()
                           .collect(toMap(Function.identity(), key -> generatePreSignedUrl(test.getSystemInfo().getPublicIP(), String.valueOf(key))));

        preSignedUrls.entrySet()
                     .forEach(entry -> uploadObject(entry.getValue().getPreSignedUrl(), snapshotMap.get(entry.getKey())));
    }

    private void uploadObject(URL url, String fileToUploadPath) {
        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");

            uploadObject(connection, new File(fileToUploadPath));

        } catch (IOException e) {
            logger.error("Failed to upload file, " + e.getMessage(), e);
            throw new RequestFailureException("Failed to upload file, " + e.getMessage(), e);
        }
    }

    private void uploadObject(HttpURLConnection connection, File fileToUpload) {
        int responseCode;

        try (DataOutputStream dataStream = new DataOutputStream(connection.getOutputStream())) {
            dataStream.write(IOUtils.toByteArray(new FileInputStream(fileToUpload)));
            responseCode = connection.getResponseCode(); // Invoke request

            if (isRequestSuccessful(responseCode)) {
                logger.info("Service returned response code " + responseCode);
            } else {
                logger.error("Failed to upload file, responseCode is " + responseCode);
                throw new RequestFailureException("Failed to upload file, responseCode is " + responseCode);
            }

        } catch (IOException e) {
            logger.error("Failed to upload file, " + e.getMessage(), e);
            throw new RequestFailureException("Failed to upload file, " + e.getMessage(), e);
        }
    }

    private boolean isRequestSuccessful(int responseCode) {
        return responseCode >= 200 && responseCode < 300;
    }

    @Override
    public SpeedTestMetaData getNextSpeedTestWebSite() {
        return requestHandler.execute(gatewayRequests.getNextSpeedTestWebSite(createTokenHeader()));
    }

    @Override
    public List<FileDownloadMetaData> getNextUrls(int numOfComparisonPerTest) {
        return requestHandler.execute(gatewayRequests.getNextUrls(createTokenHeader(), numOfComparisonPerTest));
    }

    @Override
    public PreSignedUrl generatePreSignedUrl(String userIP, String fileName) {
        userIP = userIP.replaceAll("\\.", "_");
        return requestHandler.execute(gatewayRequests.generatePreSignedUrl(createTokenHeader(), userIP, fileName));
    }

    private void handleError(int statusCode, String message) throws LoginException {
        try {
            switch (statusCode) {
                case 401:
                    ErrorMessage errorMessage = new ObjectMapper().readValue(message, ErrorMessage.class);
                    throw new LoginException(errorMessage.getError() + " " + errorMessage.getMessage());


                default:
                    throw new RequestFailureException(message, statusCode);
            }

        } catch (IOException e) {
            throw new RequestFailureException(message, statusCode);
        }
    }

    private Map<String, String> createTokenHeader() {
        Map<String, String> tokenHeader = new HashMap<>();
        tokenHeader.put(TOKEN_HEADER, token);

        return tokenHeader;
    }
}
