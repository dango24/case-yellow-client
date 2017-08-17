package caseyellow.client.sevices.googlevision;

import java.util.List;

public class Feature {

    private static final String DEFAULT_TYPE = "TEXT_DETECTION";
    private static final String DEFAULT_MAX_RESULT = "10";

    private String type;
    private String maxResults;

    public Feature() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(String maxResults) {
        this.maxResults = maxResults;
    }

    public static Feature createDefaultFeature() {
        Feature feature = new Feature();
        feature.setMaxResults(DEFAULT_MAX_RESULT);
        feature.setType(DEFAULT_TYPE);

        return feature;
    }
}
