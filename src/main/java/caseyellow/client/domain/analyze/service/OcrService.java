package caseyellow.client.domain.analyze.service;

import caseyellow.client.domain.analyze.model.OcrResponse;
import caseyellow.client.exceptions.OcrParsingException;
import caseyellow.client.exceptions.RequestFailureException;

import java.io.IOException;

public interface OcrService {
    OcrResponse parseImage(String imgPath) throws IOException, OcrParsingException, RequestFailureException;
}
