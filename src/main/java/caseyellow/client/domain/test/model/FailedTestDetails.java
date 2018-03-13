package caseyellow.client.domain.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FailedTestDetails {

    private String ip;
    private String path;
    private String errorMessage;
    private String clientVersion;

    private FailedTestDetails(String ip, String path, String errorMessage) {
        this.path = path;
        this.errorMessage = errorMessage;
        this.ip = ip;
    }

    public static class FailedTestDetailsBuilder {

        private String ip;
        private String path;
        private String errorMessage;

        public FailedTestDetailsBuilder() {
        }

        public FailedTestDetailsBuilder addIp(String ip) {
            this.ip = ip;
            return this;
        }

        public FailedTestDetailsBuilder addPath(String path) {
            this.path = path;
            return this;
        }

        public FailedTestDetailsBuilder addErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public FailedTestDetails build() {
            return new FailedTestDetails(ip, path, errorMessage);
        }
    }
}
