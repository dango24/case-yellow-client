package caseyellow.client.domain.website.model;

/**
 * Created by dango on 6/2/17.
 */
public class FastSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "https://www.fast.com/";
    }

    @Override
    public String getIdentifier() {
        return "fast";
    }

    @Override
    public boolean isFlashSupported() {
        return false;
    }
}
