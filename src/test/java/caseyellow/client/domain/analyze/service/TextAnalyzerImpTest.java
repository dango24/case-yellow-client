package caseyellow.client.domain.analyze.service;

import caseyellow.client.domain.analyze.model.WordIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TextAnalyzerImpTest {

    private TextAnalyzerService textAnalyzerService;

    @Autowired
    public void setTextAnalyzerService(TextAnalyzerService textAnalyzerService) {
        this.textAnalyzerService = textAnalyzerService;
    }

    @Test
    public void isDescriptionExist() throws Exception {
        TextAnalyzerImp textAnalyzerImp = new TextAnalyzerImp();
        Set<WordIdentifier> textIdentifiers;
        WordIdentifier wordIdentifier1 = new WordIdentifier("Mbps", 2);
        WordIdentifier wordIdentifier2 = new WordIdentifier("קישוריות", -1);

        textIdentifiers = new HashSet<>(Arrays.asList(wordIdentifier1, wordIdentifier2));
        textAnalyzerService.isDescriptionExist(null, null);
//        Dango dango = new ObjectMapper().readValue(new File("C:\\Users\\Dan\\Desktop\\dangooo.json"), Dango.class);
//        textAnalyzerImp.isDescriptionExist(textIdentifiers, dango.getDango());
    }

}
