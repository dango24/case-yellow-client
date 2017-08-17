package caseyellow.client.domain.website.model;

/**
 * Created by dango on 6/2/17.
 */
public class FastSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "https://www.fast.com/";
    }

    @Override
    public String getIdentifier() {
        return "fast";
    }

    @Override
    public String buttonId() {
        return null;
    }

    @Override
    public String finishIdentifier() {
        return "class=compare-ookla-text"; // className
    }

    @Override
    public String finishTextIdentifier() {
        return "Compare on\nSPEEDTEST.NET";
    }

    @Override
    public boolean haveStartButton() {
        return false;
    }

    @Override
    public boolean isFlashAble() {
        return false;
    }
}
