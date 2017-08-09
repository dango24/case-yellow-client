package caseyellow.client.common;

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

import static caseyellow.client.common.Utils.getFileFromResources;

/**
 * Created by dango on 6/2/17.
 */
@Component
@ConfigurationProperties
public class Mapper {

    enum ResolutionPropertiesType {
        FINISH_TEST, START_TEST
    }

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
        resolutionPropertiesMapper = new ObjectMapper().readValue(getFileFromResources(resolutionPropertiesMapperPath), ResolutionPropertiesMapper.class);
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
}

class ResolutionPropertiesMapper {

    private Map<String, ResolutionWebPageData> resolutionPropertiesMapper;

    public ResolutionPropertiesMapper() {
        resolutionPropertiesMapper = new HashMap<>();
    }

    public ResolutionPropertiesMapper(Map<String, ResolutionWebPageData> resolutionPropertiesMapper) {
        this.resolutionPropertiesMapper = resolutionPropertiesMapper;
    }

    public Map<String, ResolutionWebPageData> getResolutionPropertiesMapper() {
        return resolutionPropertiesMapper;
    }

    public void setResolutionPropertiesMapper(Map<String, ResolutionWebPageData> resolutionPropertiesMapper) {
        this.resolutionPropertiesMapper = resolutionPropertiesMapper;
    }

    public int getPixelScrollDown(String identifier, String screenResolution) {
        ResolutionWebPageData resolutionWebPageData = resolutionPropertiesMapper.get(identifier);

        if (resolutionWebPageData != null) {
            return resolutionWebPageData.getPixelScrollDown(screenResolution);
        } else {
            return 0;
        }
    }

}

class ResolutionWebPageData {

    private Map<String, ResolutionPropertiesWrapper> resolutionPropertiesMap;

    public ResolutionWebPageData() {
        resolutionPropertiesMap = new HashMap<>();
    }

    public ResolutionWebPageData(Map<String, ResolutionPropertiesWrapper> resolutionPropertiesMap) {
        this.resolutionPropertiesMap = resolutionPropertiesMap;
    }

    public Map<String, ResolutionPropertiesWrapper> getResolutionPropertiesMap() {
        return resolutionPropertiesMap;
    }

    public void setResolutionPropertiesMap(Map<String, ResolutionPropertiesWrapper> resolutionPropertiesMap) {
        this.resolutionPropertiesMap = resolutionPropertiesMap;
    }

    public int getPixelScrollDown(String screenResolution) {
        return resolutionPropertiesMap.getOrDefault(screenResolution, new ResolutionPropertiesWrapper()).getCentralized();
    }

}
