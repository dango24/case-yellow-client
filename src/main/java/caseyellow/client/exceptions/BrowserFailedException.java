package caseyellow.client.exceptions;

/**
 * Created by dango on 7/3/17.
 */
public class BrowserFailedException extends Exception {

    public BrowserFailedException(String message) {
        super(message);
    }

    public BrowserFailedException(String message, Exception e) {
        super(message, e);
    }
}
