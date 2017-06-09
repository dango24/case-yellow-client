package app.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by dango on 6/2/17.
 */
@Component
public class Utils {

    // Logger
    private Logger log = Logger.getLogger(Utils.class);

    // Fields

    @Autowired
    private ApplicationContext appContext;
    private Map<String, String> urlsMap;

    // Constructor
    public Utils() {
        urlsMap = ((Utils.UrlName)appContext.getBean("urls")).getUrlsInfo();
    }

    // Methods

    public String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    public List<String> getUrls() {
        return new ArrayList<>(urlsMap.keySet());
    }

    public String getFileNameFromUrl(String urlStr) {
        return urlsMap.get(urlStr);
    }

    @Component
    @ConfigurationProperties
    public static class UrlName {

        private Map<String, String> urlInfo = new HashMap<>();

        public UrlName() {
        }

        public UrlName(Map<String, String> urlInfo) {
            this.urlInfo = urlInfo;
        }

        public Map<String, String> getUrlsInfo() {
            return this.urlInfo;
        }

        public Map<String, String> getUrlInfo() {
            return urlInfo;
        }

        public void setUrlInfo(Map<String, String> urlInfo) {
            this.urlInfo = urlInfo;
        }
    }
}


