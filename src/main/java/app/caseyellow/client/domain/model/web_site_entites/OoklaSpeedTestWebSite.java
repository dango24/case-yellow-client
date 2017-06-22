package app.caseyellow.client.domain.model.web_site_entites;

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
    public int waitForTestToFinish() {
        return 30_000;
    }
}
