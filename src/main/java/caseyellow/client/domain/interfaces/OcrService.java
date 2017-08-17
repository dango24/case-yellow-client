package caseyellow.client.domain.interfaces;

import caseyellow.client.infrastructre.image.comparison.OcrResponse;

import java.io.IOException;

public interface OcrService {
    OcrResponse parseImage(String imgPath) throws IOException;
}
