package caseyellow.client.domain.website.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeedTestResult {

    private String result;
    private String snapshot;

    public SpeedTestResult(String result, File snapshotFile) {
        this(result, snapshotFile.getAbsolutePath());
    }
}
