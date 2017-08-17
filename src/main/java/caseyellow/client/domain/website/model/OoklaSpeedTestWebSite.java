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
    public String getIdentifier() {
        return "ookla";
    }

    @Override
    public String buttonId() {
        return null;
    }

    @Override
    public String finishIdentifier() {
        return null;
    }

    @Override
    public String finishTextIdentifier() {
        return null;
    }

    @Override
    public boolean haveStartButton() {
        return true;
    }

    @Override
    public boolean isFlashAble() {
        return true;
    }
}
