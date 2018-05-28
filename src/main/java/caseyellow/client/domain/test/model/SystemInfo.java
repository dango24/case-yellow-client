package caseyellow.client.domain.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dan on 12/10/2016.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemInfo {

    private String operatingSystem;
    private String browser;
    private String publicIP;
    private String connection; // LAN / Wifi connection

    @Override
    public String toString() {
        return "{" +
                "operatingSystem = '" + operatingSystem + '\'' +
                ", browser = '" + browser + '\'' +
                ", publicIP = '" + publicIP + '\'' +
                ", connection = '" + connection + '\'' +
                '}';
    }
}
