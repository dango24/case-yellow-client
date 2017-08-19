package caseyellow.client.domain.website.model;

import caseyellow.client.domain.analyze.model.WordIdentifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dango on 6/2/17.
 */
public class ATNTSpeedTestWebSite implements SpeedTestWebSite {

    @Override
    public String webSiteUrl() {
        return "http://speedtest.att.com/speedtest/";
    }

    @Override
    public String getIdentifier() {
        return "atnt";
    }

    @Override
    public String buttonId() {
        return "DRWidgetInitiate";
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
        return "class=Resultstitle";
    }

    @Override
    public String finishTextIdentifier() {
        return "Your Test Results";
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
        return 100;
    }
}
