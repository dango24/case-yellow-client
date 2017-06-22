package app.caseyellow.client.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by dango on 6/2/17.
 */
@Component
@ConfigurationProperties
public class UrlMapper {

    // Fields
    private Map<String, String> urlInfo = new HashMap<>();

    // Constructor
    public UrlMapper() {}

    // Methods

    public List<String> getUrls() {
        return new ArrayList<>(urlInfo.keySet());
    }

    public String getFileNameFromUrl(String urlStr) {
        return urlInfo.get(urlStr);
    }
}