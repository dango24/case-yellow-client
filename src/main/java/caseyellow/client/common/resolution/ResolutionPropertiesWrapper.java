package caseyellow.client.common.resolution;

/**
 * Created by Dan on 7/22/2017.
 */

public class ResolutionPropertiesWrapper {

    private int centralized;
    private ResolutionProperties startButtonResolutionProperties;
    private ResolutionProperties finishTestResolutionProperties;

    public ResolutionPropertiesWrapper() {
    }

    public int getCentralized() {
        return centralized;
    }

    public void setCentralized(int centralized) {
        this.centralized = centralized;
    }

    public ResolutionProperties getStartButtonResolutionProperties() {
        return startButtonResolutionProperties;
    }

    public void setStartButtonResolutionProperties(ResolutionProperties startButtonResolutionProperties) {
        this.startButtonResolutionProperties = startButtonResolutionProperties;
    }

    public ResolutionProperties getFinishTestResolutionProperties() {
        return finishTestResolutionProperties;
    }

    public void setFinishTestResolutionProperties(ResolutionProperties finishTestResolutionProperties) {
        this.finishTestResolutionProperties = finishTestResolutionProperties;
    }

}
