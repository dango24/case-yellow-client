package caseyellow.client.domain.website.model;

import caseyellow.client.exceptions.WebSiteDownloadInfoException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
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
public class SpeedTestWebSite {

    @Expose
    private String key;

    @Expose
    private String webSiteDownloadInfoSnapshot;

    @Expose
    private String message;

    private boolean succeed;
    private String urlAddress;
    private String speedTestIdentifier;
    private String nonFlashResult;
    private String path;
    private long startMeasuringTimestamp;

    public SpeedTestWebSite(String speedTestIdentifier,
                            boolean succeed,
                            long startDownloadingTime,
                            String webSiteDownloadInfoSnapshot,
                            String urlAddress,
                            String result,
                            String message,
                            String md5) {

        this.succeed = succeed;
        this.speedTestIdentifier = speedTestIdentifier;
        this.startMeasuringTimestamp = startDownloadingTime;
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
        this.urlAddress = urlAddress;
        this.nonFlashResult = result;
        this.message = message;
        this.key = md5;
    }

    public static class SpeedTestWebSiteDownloadInfoBuilder {

        private boolean succeed;
        private long startDownloadingTime;
        private String speedTestIdentifier;
        private String urlAddress;
        private String nonFlashResult;
        private String webSiteDownloadInfoSnapshot;
        private String message;
        private String md5;

        public SpeedTestWebSiteDownloadInfoBuilder(String speedTestIdentifier) {
            this.speedTestIdentifier = speedTestIdentifier;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setSucceed() {
            this.succeed = true;
            return this;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setFailure() {
            this.succeed = false;
            return this;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setURL(String urlAddress) {
            this.urlAddress = urlAddress;
            return this;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setMD5(String md5) {
            this.md5 = md5;
            return this;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setStartDownloadingTimeSnapshot(long startDownloadingTime) {
            this.startDownloadingTime = startDownloadingTime;
            return this;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setWebSiteDownloadInfoSnapshot(String webSiteDownloadInfoSnapshot) {
            this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
            return this;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setNonFlashResult(String result) {
            this.nonFlashResult = result;
            return this;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public SpeedTestWebSite build() {

                return new SpeedTestWebSite(speedTestIdentifier, succeed,
                                            startDownloadingTime, webSiteDownloadInfoSnapshot,
                                            urlAddress, nonFlashResult, message, md5);
        }

    }
}
