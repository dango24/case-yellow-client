package caseyellow.client.exceptions;

/**
 * Created by dango on 7/3/17.
 */
public class BrowserCommandFailedException extends Exception {

    public BrowserCommandFailedException(String message) {
        super(message);
    }

    public BrowserCommandFailedException(String message, Exception e) {
    }
}
