package caseyellow.client.domain.interfaces;

import caseyellow.client.exceptions.OcrParsingException;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.infrastructre.image.recognition.OcrResponse;

import java.io.IOException;

public interface OcrService extends ExternalService {
    OcrResponse parseImage(String imgPath) throws IOException, OcrParsingException, RequestFailureException;
}
