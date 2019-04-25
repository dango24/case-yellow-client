package caseyellow.client.domain.file.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadedFileDetails {

    private long fileDownloadedDurationTimeInMs;
    private String traceRouteOutputPreviousDownloadFile;
    private String traceRouteOutputAfterDownloadFile;
    private Map<String, List<String>> headers;
}
