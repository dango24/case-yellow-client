package caseyellow.client.domain.website.model;

import caseyellow.client.domain.analyze.model.WordIdentifier;

import java.util.Set;

public class SpeedTestFlashMetaData {

    private Set<WordIdentifier> buttonIds;
    private Set<WordIdentifier> finishIdentifiers;

    public SpeedTestFlashMetaData() {
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
