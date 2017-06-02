package speed.test.web.site;

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
}
