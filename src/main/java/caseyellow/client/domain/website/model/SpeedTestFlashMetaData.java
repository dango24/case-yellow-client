package caseyellow.client.domain.website.model;

import caseyellow.client.domain.analyze.model.WordIdentifier;

import java.util.Set;

public class SpeedTestFlashMetaData {

    private String finishIdentifier;
    private int maxAttempts;
    private Set<WordIdentifier> buttonIds;
    private Set<WordIdentifier> finishIdentifiers;

    public SpeedTestFlashMetaData() {
    }

    public String getFinishIdentifier() {
        return finishIdentifier;
    }

    public void setFinishIdentifier(String finishIdentifier) {
        this.finishIdentifier = finishIdentifier;
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

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
