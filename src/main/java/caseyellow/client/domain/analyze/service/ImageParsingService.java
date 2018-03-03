package caseyellow.client.domain.analyze.service;

import caseyellow.client.domain.analyze.model.ImageClassificationStatus;
import caseyellow.client.domain.analyze.model.OcrResponse;
import caseyellow.client.domain.analyze.model.VisionRequest;
import caseyellow.client.exceptions.AnalyzeException;
import caseyellow.client.exceptions.OcrParsingException;
import caseyellow.client.exceptions.RequestFailureException;

import java.io.IOException;

public interface ImageParsingService {
    OcrResponse parseImage(String imgPath) throws IOException, OcrParsingException, RequestFailureException;
    ImageClassificationStatus classifyImage(String identifier, VisionRequest visionRequest) throws AnalyzeException;
}
