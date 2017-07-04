package caseyellow.client.domain.website.model;

/**
 * Created by dango on 6/2/17.
 */
public class OoklaSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://www.speedtest.net/";
    }

    @Override
    public String startTestButton() {
        return "startTestButtonOokla.PNG";
    }

    @Override
    public String getIdentifier() {
        return "ookla";
    }

    @Override
    public String testFinishIdentifier() {
        return "finishTestIdentifier_ookla.PNG";
    }

    @Override
    public int waitForTestToFinishInSec() {
        return 60;
    }
}
