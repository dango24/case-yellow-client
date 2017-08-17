package caseyellow.client.domain.website.model;

/**
 * Created by dango on 6/2/17.
 */
public interface SpeedTestWebSite {
    String webSiteUrl();
    String getIdentifier();
    String buttonId();
    String finishIdentifier();
    String finishTextIdentifier();
    boolean haveStartButton();
    boolean isFlashAble();
}
