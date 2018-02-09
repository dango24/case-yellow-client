package caseyellow.client.domain.website.model;

import caseyellow.client.common.Utils;
import caseyellow.client.exceptions.WebSiteDownloadInfoException;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dan on 12/10/2016.
 */
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

    public SpeedTestWebSite() {
    }

    public SpeedTestWebSite(String speedTestIdentifier,
                            boolean succeed,
                            long startDownloadingTime,
                            String webSiteDownloadInfoSnapshot,
                            String urlAddress,
                            String result,
                            String message) throws IOException, NoSuchAlgorithmException {

        this.succeed = succeed;
        this.speedTestIdentifier = speedTestIdentifier;
        this.startMeasuringTimestamp = startDownloadingTime;
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
        this.urlAddress = urlAddress;
        this.nonFlashResult = result;
        this.message = message;
        this.key = Utils.convertToMD5(new File(webSiteDownloadInfoSnapshot));
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public String getSpeedTestIdentifier() {
        return speedTestIdentifier;
    }

    public void setSpeedTestIdentifier(String speedTestIdentifier) {
        this.speedTestIdentifier = speedTestIdentifier;
    }

    public long getStartMeasuringTimestamp() {
        return startMeasuringTimestamp;
    }

    public void setStartMeasuringTimestamp(long startMeasuringTimestamp) {
        this.startMeasuringTimestamp = startMeasuringTimestamp;
    }

    public String getWebSiteDownloadInfoSnapshot() {
        return webSiteDownloadInfoSnapshot;
    }

    public void setWebSiteDownloadInfoSnapshot(String webSiteDownloadInfoSnapshot) {
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNonFlashResult() {
        return nonFlashResult;
    }

    public void setNonFlashResult(String nonFlashResult) {
        this.nonFlashResult = nonFlashResult;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "SpeedTestWebSite{" +
                "succeed=" + succeed +
                ", speedTestIdentifier='" + speedTestIdentifier + '\'' +
                ", startMeasuringTimestamp=" + startMeasuringTimestamp +
                ", webSiteDownloadInfoSnapshot='" + webSiteDownloadInfoSnapshot + '\'' +
                '}';
    }

    public static class SpeedTestWebSiteDownloadInfoBuilder {

        private boolean succeed;
        private long startDownloadingTime;
        private String speedTestIdentifier;
        private String urlAddress;
        private String nonFlashResult;
        private String webSiteDownloadInfoSnapshot;
        private String message;

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
            try {
                return new SpeedTestWebSite(speedTestIdentifier, succeed,
                                            startDownloadingTime, webSiteDownloadInfoSnapshot,
                                            urlAddress, nonFlashResult, message);

            } catch (IOException | NoSuchAlgorithmException e) {
                throw new WebSiteDownloadInfoException("Failed to build SpeedTestWebSite, cause: " + e.getMessage(), e);
            }
        }

    }
}
