package caseyellow.client.domain.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SnapshotMetadata {

    private String hash;
    private String s3Path;
    private long timestamp;

    public SnapshotMetadata(String hash, String s3Path) {
        this.hash = hash;
        this.s3Path = s3Path;
        this.timestamp = System.currentTimeMillis();
    }
}
