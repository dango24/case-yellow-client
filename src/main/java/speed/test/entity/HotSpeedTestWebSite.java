package speed.test.entity;

/**
 * Created by dango on 6/2/17.
 */
public class HotSpeedTestWebSite implements SpeedTestWebSite {

    public String webSiteUrl() {
        return "http://www.hot.net.il/heb/Internet/speed/";
    }

    public String startTestButton() {
        return null;
    }
}
