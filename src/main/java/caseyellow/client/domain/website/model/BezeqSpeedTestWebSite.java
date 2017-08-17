package caseyellow.client.domain.website.model;

/**
 * Created by dango on 6/2/17.
 */
public class BezeqSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://www.bezeq.co.il/internetandphone/internet/speedtest/";
    }

    @Override
    public String getIdentifier() {
        return "bezeq";
    }

    @Override
    public String buttonId() {
        return null;
    }

    @Override
    public String finishIdentifier() {
        return "בדוק";
    }

    @Override
    public String finishTextIdentifier() {
        return null;
    }

    @Override
    public boolean haveStartButton() {
        return false;
    }

    @Override
    public boolean isFlashAble() {
        return true;
    }
}
