package speed.test.web.site.entities;

/**
 * Created by dango on 6/2/17.
 */
public class BezeqSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://www.bezeq.co.il/internetandphone/internet/speedtest/";
    }

    @Override
    public String startTestButton() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "bezeq";
    }

    @Override
    public int waitForTestToFinish() {
        return 45_000;
    }
}
