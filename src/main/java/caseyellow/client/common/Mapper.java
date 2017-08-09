package caseyellow.client.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sikuli.script.Match;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

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

    public void addStartTestResolutionProperties(String webSiteBtnIdentifier, String screenResolution, Match match) throws IOException, URISyntaxException {
        addResolutionProperties(webSiteBtnIdentifier, screenResolution, match, ResolutionPropertiesType.START_TEST);
        saveToDisk();
    }

    public void addFinishTestResolutionProperties(String identifier, String resolution, Match resolutionProperties) throws IOException, URISyntaxException {
        addResolutionProperties(identifier, resolution, resolutionProperties, ResolutionPropertiesType.FINISH_TEST);
        saveToDisk();
    }

    private void addResolutionProperties(String identifier, String resolution, Match resolutionProperties, ResolutionPropertiesType resolutionPropertiesType) {
        resolutionPropertiesMapper.addResolutionProperties(identifier, resolution, resolutionProperties, resolutionPropertiesType);
    }

    private void saveToDisk() throws IOException, URISyntaxException {
        URL resource = Mapper.class.getResource("/" + resolutionPropertiesMapperPath);
        String content = new ObjectMapper().writeValueAsString(resolutionPropertiesMapper);
        Files.write(Paths.get(resource.toURI()), content.getBytes());
    }

    public int getPixelScrollDown(String identifier, String screenResolution) {

        if (identifier.equals("hot")) {
            return 250;
        }

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

    public void addResolutionProperties(String identifier, String resolution, Match resolutionProperties, Mapper.ResolutionPropertiesType resolutionPropertiesType) {
        ResolutionWebPageData resolutionWebPageData;

        if (!resolutionPropertiesMapper.containsKey(identifier)) {
            resolutionPropertiesMapper.put(identifier, new ResolutionWebPageData());
        }
        resolutionPropertiesMapper.get(identifier).addResolutionProperties(resolution, resolutionProperties, resolutionPropertiesType);
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

    public void addResolutionProperties(String resolution, Match match, Mapper.ResolutionPropertiesType resolutionPropertiesType) {
        ResolutionProperties resolutionProperties = createResolutionProperties(match);
        ResolutionPropertiesWrapper resolutionPropertiesWrapper = resolutionPropertiesMap.get(resolution);

        if (resolutionPropertiesWrapper == null) {
            resolutionPropertiesMap.put(resolution, new ResolutionPropertiesWrapper());
            resolutionPropertiesWrapper = resolutionPropertiesMap.get(resolution);
        }

        switch (resolutionPropertiesType) {

            case FINISH_TEST:
                resolutionPropertiesWrapper.setFinishTestResolutionProperties(resolutionProperties);
                break;

            case START_TEST:
                resolutionPropertiesWrapper.setStartButtonResolutionProperties(resolutionProperties);
        }
    }

    private ResolutionProperties createResolutionProperties(Match match) {
        return new ResolutionProperties(new Point(match.getTarget().getX(), match.getTarget().getY()),
                new Coordinates(match.getX(), match.getY(), match.getH(), match.getW()));
    }
}
