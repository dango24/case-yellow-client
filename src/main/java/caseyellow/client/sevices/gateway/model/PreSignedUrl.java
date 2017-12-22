package caseyellow.client.sevices.gateway.model;

import java.net.URL;

public class PreSignedUrl {

    private URL preSignedUrl;
    private String key;

    public PreSignedUrl() {
    }

    public PreSignedUrl(URL preSignedUrl, String key) {
        this.preSignedUrl = preSignedUrl;
        this.key = key;
    }

    public URL getPreSignedUrl() {
        return preSignedUrl;
    }

    public void setPreSignedUrl(URL preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
