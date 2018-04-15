package caseyellow.client.domain.analyze.service;

import caseyellow.client.domain.analyze.model.DescriptionMatch;
import caseyellow.client.domain.analyze.model.HTMLParserResult;
import caseyellow.client.exceptions.AnalyzeException;
import caseyellow.client.exceptions.BrowserFailedException;

public interface TextAnalyzerService {
    DescriptionMatch isDescriptionExist(String identifier, boolean startTest, String screenshot) throws AnalyzeException;
    HTMLParserResult parseHtml(String identifier, String htmlPayload, String screenshot) throws BrowserFailedException;
}
