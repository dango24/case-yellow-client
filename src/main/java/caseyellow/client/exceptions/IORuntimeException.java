package caseyellow.client.exceptions;

public class IORuntimeException extends RuntimeException {

    private static final int ERROR_CODE = 1;

    public IORuntimeException(String message) {
        super(message);
    }

    public IORuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
