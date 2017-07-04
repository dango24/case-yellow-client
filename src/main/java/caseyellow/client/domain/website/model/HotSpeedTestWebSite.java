package caseyellow.client.domain.website.model;

/**
 * Created by dango on 6/2/17.
 */
public class HotSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://www.hot.net.il/heb/Internet/speed/";
    }

    @Override
    public String startTestButton() {
        return "startTestButtonHot.jpg";
    }

    @Override
    public String getIdentifier() {
        return "hot";
    }

    @Override
    public String testFinishIdentifier() {
        return null;
    }

    @Override
    public int waitForTestToFinishInSec() {
        return 60;
    }
}
