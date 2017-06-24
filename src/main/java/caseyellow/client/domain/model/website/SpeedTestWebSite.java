package caseyellow.client.domain.model.website;

/**
 * Created by dango on 6/2/17.
 */
public interface SpeedTestWebSite {
    String webSiteUrl();
    String startTestButton();
    String getIdentifier();
    int waitForTestToFinish();
}
