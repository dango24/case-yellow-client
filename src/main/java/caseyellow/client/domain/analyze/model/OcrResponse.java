package caseyellow.client.domain.analyze.model;

import caseyellow.client.domain.analyze.model.WordData;

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

