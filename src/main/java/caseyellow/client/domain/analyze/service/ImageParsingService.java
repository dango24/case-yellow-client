package caseyellow.client.domain.analyze.service;

import caseyellow.client.domain.analyze.model.ImageClassificationResult;
import caseyellow.client.domain.analyze.model.VisionRequest;
import caseyellow.client.exceptions.AnalyzeException;

public interface ImageParsingService {
    ImageClassificationResult classifyImage(String identifier, VisionRequest visionRequest) throws AnalyzeException;
}
