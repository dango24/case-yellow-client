package caseyellow.client.domain.website.model;

/**
 * Created by dango on 6/2/17.
 */
public class ATNTSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://speedtest.att.com/speedtest/";
    }

    @Override
    public String getIdentifier() {
        return "atnt";
    }

    @Override
    public boolean isFlashSupported() {
        return true;
    }
}
