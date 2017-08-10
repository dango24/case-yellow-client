package caseyellow.client.common.resolution;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dan on 8/10/2017.
 */
public class ResolutionWebPageData {

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

    public ResolutionProperties getStartButtonResolutionProperties(String screenResolution) {
        ResolutionPropertiesWrapper resolutionPropertiesWrapper = resolutionPropertiesMap.get(screenResolution);

        return resolutionPropertiesWrapper.getStartButtonResolutionProperties();
    }

    public ResolutionProperties getFinishIdentifierImg(String screenResolution) {
        ResolutionPropertiesWrapper resolutionPropertiesWrapper = resolutionPropertiesMap.get(screenResolution);

        return resolutionPropertiesWrapper.getFinishTestResolutionProperties();
    }
}

