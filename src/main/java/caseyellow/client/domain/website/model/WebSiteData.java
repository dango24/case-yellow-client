package caseyellow.client.domain.website.model;

import caseyellow.client.common.ResolutionPropertiesWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dan on 7/22/2017.
 */

public class WebSiteData {

    private String identifier;
    private String resolution;
    private Map<String, ResolutionPropertiesWrapper> resolutionProperties;

    public WebSiteData() {
        this(null, null);
    }

    public WebSiteData(String identifier, String resolution) {
        this.identifier = identifier;
        this.resolution = resolution;
        resolutionProperties = new HashMap<>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Map<String, ResolutionPropertiesWrapper> getResolutionProperties() {
        return resolutionProperties;
    }

    public void setResolutionProperties(Map<String, ResolutionPropertiesWrapper> resolutionProperties) {
        this.resolutionProperties = resolutionProperties;
    }
}