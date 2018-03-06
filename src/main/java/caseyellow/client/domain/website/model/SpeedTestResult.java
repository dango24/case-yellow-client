package caseyellow.client.domain.website.model;

public class SpeedTestResult {

    private String result;
    private String snapshot;

    public SpeedTestResult() {
    }

    public SpeedTestResult(String result, String snapshot) {
        this.result = result;
        this.snapshot = snapshot;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }
}
