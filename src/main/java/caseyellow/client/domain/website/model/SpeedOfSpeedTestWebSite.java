package caseyellow.client.domain.website.model;

import caseyellow.client.domain.analyze.model.WordIdentifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dango on 6/2/17.
 */
public class SpeedOfSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://www.speedof.me/";
    }

    @Override
    public String getIdentifier() {
        return "speedof";
    }

    @Override
    public String buttonId() {
        return "btnStart";
    }

    @Override
    public Set<WordIdentifier> buttonIds() {
        return null;
    }

    @Override
    public Set<WordIdentifier> finishIdentifiers() {
        return null;
    }

    @Override
    public String finishIdentifier() {
        return "id=msgContainer3";
    }

    @Override
    public String finishTextIdentifier() {
        return "Click 'Share' to share this result";
    }

    @Override
    public boolean haveStartButton() {
        return true;
    }

    @Override
    public boolean isFlashAble() {
        return false;
    }

    @Override
    public int centralized() {
        return 0;
    }
}
