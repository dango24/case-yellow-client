package caseyellow.client.domain.analyze.service;

import caseyellow.client.domain.analyze.model.DescriptionMatch;
import caseyellow.client.exceptions.AnalyzeException;
import caseyellow.client.exceptions.BrowserFailedException;

public interface TextAnalyzerService {
    DescriptionMatch isDescriptionExist(String identifier, boolean startTest, String screenshot) throws AnalyzeException;
    String retrieveResultFromHtml(String identifier, String htmlPayload) throws BrowserFailedException;
}
