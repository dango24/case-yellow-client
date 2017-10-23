package caseyellow.client.domain.website.model;


public class SpeedTestNonFlashMetaData {

    private String buttonId;
    private String finishIdentifier;
    private String finishTextIdentifier;
    private String resultLocation;
    private String resultAttribute;

    public SpeedTestNonFlashMetaData() {
    }

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getFinishIdentifier() {
        return finishIdentifier;
    }

    public void setFinishIdentifier(String finishIdentifier) {
        this.finishIdentifier = finishIdentifier;
    }

    public String getFinishTextIdentifier() {
        return finishTextIdentifier;
    }

    public void setFinishTextIdentifier(String finishTextIdentifier) {
        this.finishTextIdentifier = finishTextIdentifier;
    }

    public String getResultLocation() {
        return resultLocation;
    }

    public void setResultLocation(String resultLocation) {
        this.resultLocation = resultLocation;
    }

    public String getResultAttribute() {
        return resultAttribute;
    }

    public void setResultAttribute(String resultAttribute) {
        this.resultAttribute = resultAttribute;
    }
}
