package caseyellow.client.infrastructre.image.recognition;

import java.util.List;

public class OcrResponse {

    private List<WordData> textAnnotations;

    public OcrResponse() {
    }

    public List<WordData> getTextAnnotations() {
        return textAnnotations;
    }

    public void setTextAnnotations(List<WordData> textAnnotations) {
        this.textAnnotations = textAnnotations;
    }
}

