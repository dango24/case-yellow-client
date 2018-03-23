package caseyellow.client.domain.website.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeedTestNonFlashMetaData {

    private String buttonId;
    private String finishIdentifier;
    private String resultLocation;
    private String resultAttribute;
    private String retrieveResultFromPayloadFloat;
    private String retrieveResultFromPayloadInteger;
    private List<String> finishIdentifierKbps;
    private List<String> finishIdentifierMbps;
}
