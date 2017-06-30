package caseyellow.client.domain.model.website;

/**
 * Created by dango on 6/2/17.
 */
public class SpeedOfSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://www.speedof.me/";
    }

    @Override
    public String startTestButton() {
        return "startTestButtonSpeedof.PNG";
    }

    @Override
    public String getIdentifier() {
        return "speedof";
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
