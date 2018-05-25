package caseyellow.client.domain.website.model;

import caseyellow.client.domain.analyze.model.Point;
import caseyellow.client.domain.analyze.model.WordIdentifier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeedTestFlashMetaData {

    private String finishIdentifier;
    private int maxAttempts;
    private Point imageCenterPoint;
    private Set<WordIdentifier> buttonIds;
    private Set<WordIdentifier> finishIdentifiers;
}
