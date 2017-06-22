package app.caseyellow.client.domain.model.web_site_entites;

/**
 * Created by dango on 6/2/17.
 */
public interface SpeedTestWebSite {
    String webSiteUrl();
    String startTestButton();
    String getIdentifier();
    int waitForTestToFinish();
}
