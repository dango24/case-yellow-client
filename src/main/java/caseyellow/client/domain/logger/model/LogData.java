package caseyellow.client.domain.logger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogData {

    private String date;
    private String thread;
    private String level;
    private String classL;
    private String correlationId;
    private String message;
    private String user;
    private String version;

    @Builder(builderMethodName = "logDataDogBuilder")
    public static LogData newLogData(String date, String thread, String level,
                                     String classL, String correlationId, String message,
                                     String user, String version) {

        return new LogData(date, thread, level, classL, correlationId, message, user, version);
    }
}
