package caseyellow.client.domain.analyze.service;

import caseyellow.client.domain.analyze.model.DescriptionMatch;
import caseyellow.client.domain.analyze.model.WordIdentifier;
import caseyellow.client.exceptions.AnalyzeException;
import caseyellow.client.domain.analyze.model.WordData;
import caseyellow.client.exceptions.BrowserFailedException;

import java.util.List;
import java.util.Set;

public interface TextAnalyzerService {
    DescriptionMatch isDescriptionExist(Set<WordIdentifier> textIdentifiers, List<WordData> words) throws AnalyzeException;
    String retrieveResultFromHtml(String htmlPayload, List<String> MbpsRegex, List<String> KbpsRegex, int groupNumber) throws BrowserFailedException;
}
