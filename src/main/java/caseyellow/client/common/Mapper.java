package caseyellow.client.common;

import caseyellow.client.common.resolution.ResolutionProperties;
import caseyellow.client.common.resolution.ResolutionPropertiesMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static caseyellow.client.common.Utils.getTempFileFromResources;

/**
 * Created by dango on 6/2/17.
 */
@Component
@ConfigurationProperties
public class Mapper {

    // Fields
    @Value("${resolutionPropertiesMapper}")
    private String resolutionPropertiesMapperPath;
    private String speedTestWebSitePackage;
    private Map<String, String> urlInfo = new HashMap<>();
    private Map<String, String> websiteInfo = new HashMap<>();
    private ResolutionPropertiesMapper resolutionPropertiesMapper;

    // Methods

    @PostConstruct
    public void init() throws IOException, URISyntaxException {
        resolutionPropertiesMapper = new ObjectMapper().readValue(getTempFileFromResources(resolutionPropertiesMapperPath), ResolutionPropertiesMapper.class);
    }

    public void setSpeedTestWebSitePackage(String speedTestWebSitePackage) {
        this.speedTestWebSitePackage = speedTestWebSitePackage;
    }

    public void setUrlInfo(Map<String, String> urlInfo) {
        this.urlInfo = urlInfo;
    }

    public void setWebsiteInfo(Map<String, String> websiteInfo) {
        this.websiteInfo = websiteInfo;
    }

    public String getSpeedTestWebSitePackage() {
        return speedTestWebSitePackage;
    }

    public Map<String, String> getUrlInfo() {
        return urlInfo;
    }

    public Map<String, String> getWebsiteInfo() {
        return websiteInfo;
    }

    public String getFileNameFromUrl(String urlStr) {
        return urlInfo.get(urlStr);
    }

    public List<String> getUrls() {
        return new ArrayList<>(urlInfo.keySet());
    }

    public List<String> getWebsiteIdentifiers() {
        return new ArrayList<>(websiteInfo.keySet());
    }

    public String getWebSiteClassFromIdentifier(String identifier) {
        return speedTestWebSitePackage + websiteInfo.get(identifier);
    }

    public int getPixelScrollDown(String identifier, String screenResolution) {
        return resolutionPropertiesMapper.getPixelScrollDown(identifier, screenResolution);
    }

    public ResolutionProperties getStartButtonResolutionProperties(String identifier) {
        return resolutionPropertiesMapper.getStartButtonResolutionProperties(identifier);
    }

    public ResolutionProperties getFinishIdentifierImg(String identifier) {
        return resolutionPropertiesMapper.getFinishIdentifierImg(identifier);
    }
}