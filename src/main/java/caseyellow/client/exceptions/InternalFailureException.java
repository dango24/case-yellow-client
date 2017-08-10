package caseyellow.client.exceptions;

/**
 * Created by Dan on 8/10/2017.
 */
public class InternalFailureException extends RuntimeException {

    public InternalFailureException(String message) {
        super(message);
    }

    public InternalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
