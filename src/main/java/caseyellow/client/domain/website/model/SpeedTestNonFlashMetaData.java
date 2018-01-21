package caseyellow.client.domain.website.model;


import java.util.List;

public class SpeedTestNonFlashMetaData {

    private String buttonId;
    private String finishIdentifier;
    private String resultLocation;
    private String resultAttribute;
    private List<String> finishIdentifierKbps;
    private List<String> finishTextIdentifier;

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

    public List<String> getFinishIdentifierKbps() {
        return finishIdentifierKbps;
    }

    public void setFinishIdentifierKbps(List<String> finishIdentifierKbps) {
        this.finishIdentifierKbps = finishIdentifierKbps;
    }

    public List<String> getFinishTextIdentifier() {
        return finishTextIdentifier;
    }

    public void setFinishTextIdentifier(List<String> finishTextIdentifier) {
        this.finishTextIdentifier = finishTextIdentifier;
    }
}
