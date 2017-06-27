package caseyellow.client.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by dango on 6/2/17.
 */
@Component
@ConfigurationProperties
public class Mapper {

    // Fields
    private String speedTestWebSitePackage;
    private Map<String, String> urlInfo = new HashMap<>();
    private Map<String, String> websiteInfo = new HashMap<>();

    // Constructor
    public Mapper() {}

    // Methods

    public String getSpeedTestWebSitePackage() {
        return speedTestWebSitePackage;
    }

    public void setSpeedTestWebSitePackage(String speedTestWebSitePackage) {
        this.speedTestWebSitePackage = speedTestWebSitePackage;
    }

    public Map<String, String> getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(Map<String, String> urlInfo) {
        this.urlInfo = urlInfo;
    }

    public Map<String, String> getWebsiteInfo() {
        return websiteInfo;
    }

    public void setWebsiteInfo(Map<String, String> websiteInfo) {
        this.websiteInfo = websiteInfo;
    }

    public String getFileNameFromUrl(String urlStr) {
        return urlInfo.get(urlStr);
    }

    public List<String> getUrls() {
        return new ArrayList<>(urlInfo.keySet());
    }

    public String getWebSiteClassFromIdentifier(String identifier) {
        return speedTestWebSitePackage + websiteInfo.get(identifier);
    }
}