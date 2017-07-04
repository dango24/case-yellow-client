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
    public String startTestButton() {
        return "startTestButtonAtnt.PNG";
    }

    @Override
    public String getIdentifier() {
        return "atnt";
    }

    @Override
    public String testFinishIdentifier() {
        return null;
    }

    @Override
    public int waitForTestToFinishInSec() {
        return 50;
    }
}
