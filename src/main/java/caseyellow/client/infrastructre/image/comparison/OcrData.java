package caseyellow.client.infrastructre.image.comparison;

import java.util.List;

public class OcrData {

    private List<OcrResponse> responses;

    public OcrData() {
    }

    public List<OcrResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<OcrResponse> responses) {
        this.responses = responses;
    }
}
