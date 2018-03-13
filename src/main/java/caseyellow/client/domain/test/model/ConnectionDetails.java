package caseyellow.client.domain.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionDetails {

    private int speed;
    private String isp;
    private String infrastructure;

    public ConnectionDetails(String infrastructure, int speed) {
        this.speed = speed;
        this.infrastructure = infrastructure;
    }

}
