package caseyellow.client.exceptions;

/**
 * Created by Dan on 7/7/2017.
 */
public class WebSiteDownloadInfoException extends RuntimeException {

    public WebSiteDownloadInfoException(String message) {
        super(message);
    }

    public WebSiteDownloadInfoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
