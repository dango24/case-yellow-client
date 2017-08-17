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
    public String getIdentifier() {
        return "hot";
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
