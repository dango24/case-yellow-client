package caseyellow.client.domain.analyze.model;

import java.util.ArrayList;
import java.util.List;

public class ImageContext {

    private final static String HEBREW = "he";
    private final static String ENGLISH = "en";

    private List<String> languageHints;

    public ImageContext() {
        languageHints = new ArrayList<>();
    }

    public List<String> getLanguageHints() {
        return languageHints;
    }

    public void setLanguageHints(List<String> languageHints) {
        this.languageHints = languageHints;
    }

    public static ImageContext createImageContextDefaultValues() {
        ImageContext imageContext = new ImageContext();
        imageContext.addLanguage(HEBREW);
        imageContext.addLanguage(ENGLISH);

        return imageContext;
    }

    public void addLanguage(String lang) {
        languageHints.add(lang);
    }
}
