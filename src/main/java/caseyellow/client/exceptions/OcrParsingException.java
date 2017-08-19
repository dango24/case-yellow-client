package caseyellow.client.exceptions;

public class OcrParsingException extends Exception {

    public OcrParsingException() {
    }

    public OcrParsingException(String message) {
        super(message);
    }

    public OcrParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
