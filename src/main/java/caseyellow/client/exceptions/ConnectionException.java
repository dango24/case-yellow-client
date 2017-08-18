package caseyellow.client.exceptions;

import java.net.UnknownHostException;

public class ConnectionException extends InternalFailureException {


    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
