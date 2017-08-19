package caseyellow.client.domain.analyze.service;

import caseyellow.client.domain.analyze.model.WordIdentifier;
import caseyellow.client.infrastructre.image.recognition.WordData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TextAnalyzerImpTest {


    @Test
    public void isDescriptionExist() throws Exception {
        TextAnalyzerImp textAnalyzerImp = new TextAnalyzerImp();
        Set<WordIdentifier> textIdentifiers;
        WordIdentifier wordIdentifier1 = new WordIdentifier("Mbps", 2);
        WordIdentifier wordIdentifier2 = new WordIdentifier("קישוריות", -1);

        textIdentifiers = new HashSet<>(Arrays.asList(wordIdentifier1, wordIdentifier2));
        Dango dango = new ObjectMapper().readValue(new File("C:\\Users\\Dan\\Desktop\\dangooo.json"), Dango.class);

        textAnalyzerImp.isDescriptionExist(textIdentifiers, dango.getDango());
    }

}

class Dango {

    private List<WordData> dango;

    public Dango() {
    }

    public List<WordData> getDango() {
        return dango;
    }

    public void setDango(List<WordData> dango) {
        this.dango = dango;
    }
}