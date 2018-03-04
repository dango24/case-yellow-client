package caseyellow.client.domain.analyze.model;

import org.springframework.util.StringUtils;

public class ImageClassificationResult {

    private String message;
    private ImageClassificationStatus status;

    public ImageClassificationResult() {
        this(null, null);
    }

    public ImageClassificationResult(ImageClassificationStatus status) {
        this(null, status);
    }

    public ImageClassificationResult(String message, ImageClassificationStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ImageClassificationStatus getStatus() {
        return status;
    }

    public void setStatus(ImageClassificationStatus status) {
        this.status = status;
    }

    public boolean displayMessage() {
        return !StringUtils.isEmpty(message);
    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
