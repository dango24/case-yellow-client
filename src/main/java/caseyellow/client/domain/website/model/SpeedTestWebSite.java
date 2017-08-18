package caseyellow.client.domain.website.model;

import caseyellow.client.domain.analyze.model.WordIdentifier;

import java.util.Set;

/**
 * Created by dango on 6/2/17.
 */
public interface SpeedTestWebSite {
    String webSiteUrl();
    String getIdentifier();
    String buttonId();
    Set<WordIdentifier> buttonIds();
    Set<WordIdentifier> finishIdentifiers();
    String finishIdentifier();
    String finishTextIdentifier();
    boolean haveStartButton();
    boolean isFlashAble();
    int centralized();
}
