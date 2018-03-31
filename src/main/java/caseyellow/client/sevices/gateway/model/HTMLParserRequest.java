package caseyellow.client.sevices.gateway.model;

import caseyellow.client.domain.analyze.model.GoogleVisionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HTMLParserRequest {

    private String payload;
    private GoogleVisionRequest googleVisionRequest;
}
