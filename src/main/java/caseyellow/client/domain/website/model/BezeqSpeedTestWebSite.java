package caseyellow.client.domain.website.model;

import caseyellow.client.domain.analyze.model.WordIdentifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    public Set<WordIdentifier> buttonIds() {
        return null;
    }

    @Override
    public Set<WordIdentifier> finishIdentifiers() {
        WordIdentifier wordIdentifier1 = new WordIdentifier("בדוק", 1);
        WordIdentifier wordIdentifier2 = new WordIdentifier("שוב", 1);

        return new HashSet<>(Arrays.asList(wordIdentifier1, wordIdentifier2));
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
        return false;
    }

    @Override
    public boolean isFlashAble() {
        return true;
    }

    @Override
    public int centralized() {
        return 0;
    }
}
