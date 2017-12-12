package caseyellow.client.sevices.gateway.model;

import java.net.URL;

public class PreSignedUrl {

    private URL preSignedUrl;

    public PreSignedUrl() {
    }

    public PreSignedUrl(URL preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }

    public URL getPreSignedUrl() {
        return preSignedUrl;
    }

    public void setPreSignedUrl(URL preSignedUrl) {
        this.preSignedUrl = preSignedUrl;
    }
}
