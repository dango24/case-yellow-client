package caseyellow.client.exceptions;

public class AnalyzeException extends Exception {

    private String snapshot;

    public AnalyzeException() {
    }

    public AnalyzeException(String message) {
        this(message, null, null);
    }

    public AnalyzeException(String message, String snapshot) {
        this(message, snapshot, null);
    }

    public AnalyzeException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public AnalyzeException(String message, String snapshot, Throwable cause) {
        super(message, cause);
        this.snapshot = snapshot;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }
}
