package speed.test.web.site;

/**
 * Created by dango on 6/2/17.
 */
public class FastSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "https://www.fast.com/";
    }

    @Override
    public String startTestButton() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "fast";
    }
}
