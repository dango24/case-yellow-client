package caseyellow.client.sevices.gateway.services;

import caseyellow.client.domain.analyze.model.*;
import caseyellow.client.domain.analyze.service.TextAnalyzerService;
import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.analyze.service.ImageParsingService;
import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.logger.model.LogData;
import caseyellow.client.domain.logger.services.CYLogger;
import caseyellow.client.domain.system.SystemService;
import caseyellow.client.domain.test.model.*;
import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.*;
import caseyellow.client.sevices.gateway.model.*;
import caseyellow.client.sevices.infrastrucre.RequestHandler;
import caseyellow.client.sevices.infrastrucre.RetrofitBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Profile("prod")
@Service("gatewayService")
public class GatewayServiceImpl implements GatewayService, DataAccessService, ImageParsingService, TextAnalyzerService {

    private static CYLogger logger = new CYLogger(GatewayServiceImpl.class);

    private static final String DELIMITER = "-";
    private static final String FILE_EXTENSION = ".png";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String TOKEN_HEADER = "Authorization";
    private static final String USER_HEADER = "Case-Yellow-User";
    private static final String USER_REGISTRATION = "user_registration";

    @Value("${gateway_url}")
    private String gatewayUrl;

    @Value("${failed_tests_dir}")
    private String failedTestsDir;

    @Value("${successful_tests_dir}")
    private String successfulTestsDir;

    @Value("${client.version}")
    private String clientVersion;

    private String user;
    private String token;
    private RequestHandler requestHandler;
    private SystemService systemService;
    private GatewayRequests gatewayRequests;

    @Autowired
    public GatewayServiceImpl(RequestHandler requestHandler, SystemService systemService) {
        this.requestHandler = requestHandler;
        this.systemService = systemService;
    }

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(gatewayUrl).build();
        gatewayRequests = retrofit.create(GatewayRequests.class);
    }

    @Override
    public LoginDetails login(AccountCredentials accountCredentials) throws IOException, LoginException {
        try {
            Map<String, String> headers = requestHandler.getResponseHeaders(gatewayRequests.login(accountCredentials));

            if (!headers.containsKey(TOKEN_HEADER)) {
                throw new LoginException("There is no authentication header at the login request");
            }

            user = accountCredentials.getUsername();
            token = headers.get(TOKEN_HEADER)
                           .replaceAll(TOKEN_PREFIX, "")
                           .trim();

            return new LoginDetails(true, isFirstRegistration(headers.get(USER_REGISTRATION)));

        } catch (RequestFailureException e) {
            handleError(e.getErrorCode(), e.getMessage());
            return LoginDetails.LoginDetailsFailed();
        }
    }

    @Override
    public SpeedTestMetaData getNextSpeedTestWebSite() throws RequestFailureException {
        return requestHandler.execute(gatewayRequests.getNextSpeedTestWebSite(createTokenHeader()));
    }

    @Override
    public List<FileDownloadProperties> getNextUrls() throws RequestFailureException {
        return requestHandler.execute(gatewayRequests.getNextUrls(createTokenHeader()));
    }

    @Override
    public PreSignedUrl generatePreSignedUrl(String key) {
        return requestHandler.execute(gatewayRequests.generatePreSignedUrl(createTokenHeader(), key));
    }

    @Override
    public void uploadLogData(LogData logData) {
        requestHandler.execute(gatewayRequests.uploadLogData(createTokenHeader(), logData));
    }

    @Override
    public int getTestLifeCycle() {
        return requestHandler.execute(gatewayRequests.getTestLifeCycle(createTokenHeader()));
    }

    @Override
    public void updateTestLifeCycle() {
        requestHandler.execute(gatewayRequests.updateTestLifeCycle(createTokenHeader()));
    }

    @Override
    public boolean runClassicTest() {
        return requestHandler.execute(gatewayRequests.runClassicTest(createTokenHeader()));
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String clientVersion() {
        return clientVersion;
    }

    @Override
    public List<String> getChromeOptionsArguments() {
        try {
            return requestHandler.execute(gatewayRequests.getChromeOptionsArguments(createTokenHeader()));

        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, List<String>> getConnectionDetails() {
        return requestHandler.execute(gatewayRequests.getConnectionDetails(createTokenHeader()));
    }

    @Override
    public void saveConnectionDetails(ConnectionDetails connectionDetails) {
        connectionDetails.setIsp(systemService.getISP());
        requestHandler.execute(gatewayRequests.saveConnectionDetails(createTokenHeader(), connectionDetails));
    }

    @Override
    public DescriptionMatch isDescriptionExist(String identifier, boolean startTest, String screenshot) throws AnalyzeException {
        try {
            GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(screenshot);
            return requestHandler.execute(gatewayRequests.isDescriptionExist(createTokenHeader(), identifier, startTest, googleVisionRequest));

        } catch (RequestFailureException | IOException e) {

            throw new AnalyzeException(e.getMessage(), screenshot);
        }
    }

    @Override
    public HTMLParserResult parseHtml(String identifier, String htmlPayload, String screenshot) throws BrowserFailedException {
        try {
            GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(screenshot);
            return requestHandler.execute(gatewayRequests.retrieveResultFromHtml(createTokenHeader(), identifier, new HTMLParserRequest(htmlPayload, googleVisionRequest)));

        } catch (RequestFailureException | IOException e) {

            throw new BrowserFailedException(e.getMessage());
        }
    }

    @Override
    public void startButtonSuccessfullyFound(String identifier, Point imageCenterPoint, VisionRequest visionRequest) {
        requestHandler.execute(gatewayRequests.startButtonSuccessfullyFound(createTokenHeader(), identifier, imageCenterPoint.getX(), imageCenterPoint.getY(), visionRequest));
    }

    @Override
    public void startButtonFailed(String identifier, Point imageCenterPoint, VisionRequest visionRequest) {
        requestHandler.execute(gatewayRequests.startButtonFailed(createTokenHeader(), identifier, imageCenterPoint.getX(), imageCenterPoint.getY(), visionRequest));
    }

    @Override
    public void saveTest(Test test) throws RequestFailureException {
        try {
            if (nonNull(test)) {

                if (test.isClassicTest()) {
                    generateSnapshotPath(test);
                    systemService.saveSnapshotHashToDisk(test);
                }

                test.setClientVersion(clientVersion);
                requestHandler.execute(gatewayRequests.saveTest(createTokenHeader(), test));

                if (test.isClassicTest()) {
                    uploadSnapshotImages(test);
                }

                logger.info(String.format("Save test succeed: %s", test));
            }
        } catch (Exception e) {
            logger.error(String.format("Failed to save test, cause: %s", e.getMessage(), e));
        }
    }

    @Override
    public void startTest(String identifier, List<String> urls) throws RequestFailureException {
        requestHandler.execute(gatewayRequests.startTest(createTokenHeader(), new StartTestDetails(user, identifier, urls)));
    }

    @Override
    public ImageClassificationResult classifyImage(String identifier, VisionRequest visionRequest) throws AnalyzeException {
        try {
            return requestHandler.execute(gatewayRequests.classifyImage(createTokenHeader(),identifier, visionRequest));

        } catch (RequestFailureException e) {
            String errorMessage = String.format("Failed to analyze image, error code: %s error message: %s", e.getErrorCode(), e.getMessage());
            logger.error(errorMessage);

            throw new AnalyzeException(errorMessage);
        }
    }

    @Override
    public void notifyFailedTest(ComparisonInfo comparisonInfo, String clientIP) throws RequestFailureException {
        FailedTestDetails failedTestDetails;

        if (isFailedSpeedTestWebSite(comparisonInfo)) {
            failedTestDetails = createFailedTestFromSpeedTestWebSite(comparisonInfo.getSpeedTestWebSite(), clientIP);
        } else {
            failedTestDetails = createFailedTestFromFileDownloadInfo(comparisonInfo.getFileDownloadInfo(), clientIP);
        }

        failedTestDetails.setClientVersion(clientVersion);
        requestHandler.execute(gatewayRequests.failedTest(createTokenHeader(), failedTestDetails));
    }

    private boolean isFailedSpeedTestWebSite(ComparisonInfo comparisonInfo) {
        return nonNull(comparisonInfo.getSpeedTestWebSite()) && !comparisonInfo.getSpeedTestWebSite().isSucceed();
    }

    @Override
    public void uploadFileToServer(String key, String fileToUpload) {
        logger.info("upload file to server: " + key);

        PreSignedUrl preSignedUrl = generatePreSignedUrl(key);
        uploadObject(preSignedUrl.getPreSignedUrl(), fileToUpload);
    }

    private FailedTestDetails createFailedTestFromSpeedTestWebSite(SpeedTestWebSite failedSpeedTestWebSite, String clientIP) {
        logger.error("Receive failed test: " + failedSpeedTestWebSite);

        PreSignedUrl preSignedUrl = generatePreSignedUrl(generateFailureKey(failedSpeedTestWebSite));
        uploadObject(preSignedUrl.getPreSignedUrl(), failedSpeedTestWebSite.getWebSiteDownloadInfoSnapshot());

        return new FailedTestDetails.FailedTestDetailsBuilder()
                                    .addIp(clientIP)
                                    .addIdentifier(failedSpeedTestWebSite.getSpeedTestIdentifier())
                                    .addErrorMessage(failedSpeedTestWebSite.getMessage())
                                    .addPath(preSignedUrl.getKey().replace(failedTestsDir, ""))
                                    .build();
    }

    private FailedTestDetails createFailedTestFromFileDownloadInfo(FileDownloadInfo failedFileDownloadInfo, String clientIP) {
        logger.error("Receive failed test: " + failedFileDownloadInfo);

        return new FailedTestDetails.FailedTestDetailsBuilder()
                                    .addIp(clientIP)
                                    .addIdentifier(failedFileDownloadInfo.getFileName())
                                    .addErrorMessage(failedFileDownloadInfo.getMessage())
                                    .addPath(failedFileDownloadInfo.getFileURL())
                                    .build();
    }

    private void generateSnapshotPath(Test test) {
        List<SpeedTestWebSite> speedTestWebSites = getSpeedTestWebSiteFromTest(test);
        speedTestWebSites.forEach(speedTest -> speedTest.setPath(generateSuccessfulKey(test.getSystemInfo().getPublicIP(), speedTest)));
    }

    private void uploadSnapshotImages(Test test) {
        List<SpeedTestWebSite> speedTestWebSites = getSpeedTestWebSiteFromTest(test);
        speedTestWebSites.forEach(speedTest -> uploadObject(generatePreSignedUrl(successfulTestsDir + speedTest.getPath()).getPreSignedUrl(), speedTest.getWebSiteDownloadInfoSnapshot()));
    }

    private List<SpeedTestWebSite> getSpeedTestWebSiteFromTest(Test test) {

        return test.getComparisonInfoTests()
                   .stream()
                   .map(ComparisonInfo::getSpeedTestWebSite)
                   .collect(toList());
    }

    private String generateSuccessfulKey(String ip, SpeedTestWebSite speedTest) {

        return new StringBuilder().append(user)
                                  .append(DELIMITER)
                                  .append(ip.replaceAll("\\.", "_"))
                                  .append(DELIMITER)
                                  .append(speedTest.getKey())
                                  .append(DELIMITER)
                                  .append(speedTest.getSpeedTestIdentifier())
                                  .append(FILE_EXTENSION)
                                  .toString();
    }

    private String generateFailureKey(SpeedTestWebSite speedTest) {

        return new StringBuilder().append(failedTestsDir)
                                  .append(user)
                                  .append(DELIMITER)
                                  .append(speedTest.getKey())
                                  .append(DELIMITER)
                                  .append(speedTest.getSpeedTestIdentifier())
                                  .append(FILE_EXTENSION)
                                  .toString();
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

        try (DataOutputStream dataStream = new DataOutputStream(connection.getOutputStream());
             InputStream inputStream = new FileInputStream(fileToUpload)) {

            dataStream.write(IOUtils.toByteArray(inputStream));
            responseCode = connection.getResponseCode(); // Invoke request

            if (isRequestSuccessful(responseCode)) {
                logger.info(String.format("Upload object: %s succeed, Service returned response code: %s", fileToUpload.getAbsolutePath(), responseCode));
            } else {
                String errorMessage = String.format("Failed to upload file: %s, responseCode is: ", fileToUpload.getAbsolutePath(), responseCode);
                logger.error(errorMessage);
                throw new RequestFailureException(errorMessage);
            }

        } catch (IOException e) {
            String errorMessage = String.format("Failed to upload file: %s, cause: %s", fileToUpload.getAbsolutePath(), e.getMessage());
            logger.error(errorMessage, e);

            throw new RequestFailureException(errorMessage, e);
        }
    }

    private boolean isRequestSuccessful(int responseCode) {
        return responseCode >= 200 && responseCode < 300;
    }

    private boolean isFirstRegistration(String registrationHeader) {
        return nonNull(registrationHeader) && Boolean.valueOf(registrationHeader);
    }

    private void handleError(int statusCode, String message) throws LoginException {
        try {
            switch (statusCode) {
                case 401:
                    ErrorMessage errorMessage = new ObjectMapper().readValue(message, ErrorMessage.class);
                    logger.error(String.format("Handle error with status code: %s, cause: %s", statusCode, errorMessage.getMessage()));

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
        tokenHeader.put(USER_HEADER, user);

        return tokenHeader;
    }
}
