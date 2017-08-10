package caseyellow.client.common.resolution;

import caseyellow.client.common.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dan on 8/10/2017.
 */
public class ResolutionPropertiesMapper {

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

    public ResolutionProperties getStartButtonResolutionProperties(String identifier) {
        String screenResolution = Utils.getScreenResolution();
        ResolutionWebPageData resolutionWebPageData = resolutionPropertiesMapper.get(identifier);

        return resolutionWebPageData.getStartButtonResolutionProperties(screenResolution);
    }

    public ResolutionProperties getFinishIdentifierImg(String identifier) {
        String screenResolution = Utils.getScreenResolution();
        ResolutionWebPageData resolutionWebPageData = resolutionPropertiesMapper.get(identifier);

        return resolutionWebPageData.getFinishIdentifierImg(screenResolution);
    }
}
