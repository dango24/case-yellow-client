package caseyellow.client.domain.website.model;

/**
 * Created by dango on 6/2/17.
 */
public class SpeedOfSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://www.speedof.me/";
    }

    @Override
    public String getIdentifier() {
        return "speedof";
    }

    @Override
    public boolean isFlashable() {
        return true;
    }
}
