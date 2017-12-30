package caseyellow.client.domain.analyze.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleVisionRequest {

    private List<VisionRequest> requests;

    public GoogleVisionRequest() {
        requests = new ArrayList<>();
    }

    public GoogleVisionRequest(String imgPath) throws IOException {
        this();
        VisionRequest visionRequest = new VisionRequest(imgPath);
        addRequest(visionRequest);
    }

    public List<VisionRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<VisionRequest> requests) {
        this.requests = requests;
    }

    public void addRequest(VisionRequest visionRequest) {
        requests.add(visionRequest);
    }
}


