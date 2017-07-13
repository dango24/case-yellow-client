package caseyellow.client.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by dango on 6/2/17.
 */
@Component
@ConfigurationProperties
public class Mapper {

//    @Value("${centralizedWebPageData}")

    // Fields
    private String speedTestWebSitePackage;
    private Map<String, String> urlInfo = new HashMap<>();
    private Map<String, String> websiteInfo = new HashMap<>();
    private CentralizedWebPage centralizedWebPage;

    // Methods

    public Mapper() throws IOException {
        String centralizedWebPageData = "centralizedWebPageData.json";
        centralizedWebPage = new ObjectMapper().readValue(Utils.getFileFromResources(centralizedWebPageData), CentralizedWebPage.class);
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

    public long getPixelScrollDown(String identifier, String resolution) {
        return centralizedWebPage.getPixelScrollDown(identifier, resolution);
    }
}

class CentralizedWebPage {

    private Set<CentralizedWebPageData> centralizedWebPageDataSet;

    public CentralizedWebPage() {
    }

    public Set<CentralizedWebPageData> getCentralizedWebPageDataSet() {
        return centralizedWebPageDataSet;
    }

    public void setCentralizedWebPageDataSet(Set<CentralizedWebPageData> centralizedWebPageDataSet) {
        this.centralizedWebPageDataSet = centralizedWebPageDataSet;
    }

    public long getPixelScrollDown(String identifier, String resolution) {

        if (!centralizedWebPageDataSet.contains(new CentralizedWebPageData(identifier))) {
            return 0;
        }

        return centralizedWebPageDataSet.stream()
                                        .filter(centralizedWebPageData -> centralizedWebPageData.getIdentifier().equals(identifier))
                                        .mapToLong(centralizedWebPageData -> centralizedWebPageData.getScrollDownPixels(resolution))
                                        .findFirst()
                                        .orElse(0L);
    }
}

class CentralizedWebPageData {

    private String identifier;
    private Map<String, Integer> values;

    public CentralizedWebPageData() {
    }

    public CentralizedWebPageData(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Map<String, Integer> getValues() {
        return values;
    }

    public void setValues(Map<String, Integer> values) {
        this.values = values;
    }

    public int getScrollDownPixels(String resolution) {
        return values.get(resolution);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CentralizedWebPageData that = (CentralizedWebPageData) o;

        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }
}