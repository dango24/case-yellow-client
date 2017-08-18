package caseyellow.client.domain.website.model;

import caseyellow.client.domain.analyze.model.WordIdentifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    public Set<WordIdentifier> buttonIds() {
        WordIdentifier wordIdentifier1 = new WordIdentifier("בדיקה", 1);
        WordIdentifier wordIdentifier2 = new WordIdentifier("התחל", 1);

        return new HashSet<>(Arrays.asList(wordIdentifier1, wordIdentifier2));
    }

    @Override
    public Set<WordIdentifier> finishIdentifiers() {
        WordIdentifier wordIdentifier1 = new WordIdentifier("Mbps", 2);
        WordIdentifier wordIdentifier2 = new WordIdentifier("קישוריות", -1);

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
        return true;
    }

    @Override
    public boolean isFlashAble() {
        return true;
    }

    @Override
    public int centralized() {
        return 250; // For 1920_1080 : 250, 1340_860 : 180
    }
}
