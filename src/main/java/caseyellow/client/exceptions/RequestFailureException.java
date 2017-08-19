package caseyellow.client.exceptions;

import static caseyellow.client.common.Mapper.USER_INTERRUPT_CODE;

public class RequestFailureException extends Exception {

    private int errorCode;

    public RequestFailureException(int errorCode) {
        this.errorCode = errorCode;
    }

    public RequestFailureException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RequestFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestFailureException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
