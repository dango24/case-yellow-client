package caseyellow.client.domain.website.model;


import caseyellow.client.domain.analyze.model.WordIdentifier;

import java.util.Set;

public class SpeedTestMetaData {

    private String webSiteUrl;
    private String identifier;
    private String buttonId;
    private String finishIdentifier;
    private String finishTextIdentifier;
    private int centralized;
    private boolean flashAble;
    private boolean haveStartButton;
    private Set<WordIdentifier> buttonIds;
    private Set<WordIdentifier> finishIdentifiers;

    public SpeedTestMetaData() {
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

    public Set<WordIdentifier> getButtonIds() {
        return buttonIds;
    }

    public void setButtonIds(Set<WordIdentifier> buttonIds) {
        this.buttonIds = buttonIds;
    }

    public Set<WordIdentifier> getFinishIdentifiers() {
        return finishIdentifiers;
    }

    public void setFinishIdentifiers(Set<WordIdentifier> finishIdentifiers) {
        this.finishIdentifiers = finishIdentifiers;
    }
}
