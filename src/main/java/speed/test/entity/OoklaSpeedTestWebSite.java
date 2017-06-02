package speed.test.entity;

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
}
