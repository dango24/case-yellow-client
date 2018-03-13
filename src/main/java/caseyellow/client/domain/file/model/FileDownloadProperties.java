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

    public FileDownloadProperties(String identifier, String url) {
        this(identifier, url, 0, null);
    }

    public FileDownloadProperties(String identifier, String url, int size, String md5) {
        this.url = url;
        this.size = size;
        this.md5 = md5;
        this.identifier = identifier;
    }
}
