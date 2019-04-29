package caseyellow.client.domain.file.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileDownloadProperties {

    private int size;
    private String url;
    private String md5;
    private String identifier;
    private int timeoutInMin;
}
