package caseyellow.client.domain.website.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeedTestMetaData {

    private String webSiteUrl;
    private String identifier;
    private int centralized;
    private boolean flashAble;
    private boolean haveStartButton;
    private SpeedTestFlashMetaData speedTestFlashMetaData;
    private SpeedTestNonFlashMetaData speedTestNonFlashMetaData;
    private List<Role> roles;

    public SpeedTestMetaData() {
        roles = new ArrayList<>();
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getCentralized() {
        return centralized;
    }

    public void setCentralized(int centralized) {
        this.centralized = centralized;
    }

    public boolean isFlashAble() {
        return flashAble;
    }

    public void setFlashAble(boolean flashAble) {
        this.flashAble = flashAble;
    }

    public boolean isHaveStartButton() {
        return haveStartButton;
    }

    public void setHaveStartButton(boolean haveStartButton) {
        this.haveStartButton = haveStartButton;
    }

    public SpeedTestFlashMetaData getSpeedTestFlashMetaData() {
        return speedTestFlashMetaData;
    }

    public void setSpeedTestFlashMetaData(SpeedTestFlashMetaData speedTestFlashMetaData) {
        this.speedTestFlashMetaData = speedTestFlashMetaData;
    }

    public SpeedTestNonFlashMetaData getSpeedTestNonFlashMetaData() {
        return speedTestNonFlashMetaData;
    }

    public void setSpeedTestNonFlashMetaData(SpeedTestNonFlashMetaData speedTestNonFlashMetaData) {
        this.speedTestNonFlashMetaData = speedTestNonFlashMetaData;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void resetAllRules() {
        if (nonNull(roles)) {
            roles.forEach(role -> role.setExecuted(false));
        }
    }
}
