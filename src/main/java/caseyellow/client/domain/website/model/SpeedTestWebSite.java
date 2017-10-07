package caseyellow.client.domain.website.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Dan on 12/10/2016.
 */
public class SpeedTestWebSite {

    private int key;
    private boolean succeed;
    private String speedTestIdentifier;
    private long startMeasuringTimestamp;

    @JsonIgnore
    private String webSiteDownloadInfoSnapshot;

    public SpeedTestWebSite() {
    }

    public SpeedTestWebSite(String speedTestIdentifier, boolean succeed, long startDownloadingTime, String webSiteDownloadInfoSnapshot) {
        this.succeed = succeed;
        this.speedTestIdentifier = speedTestIdentifier;
        this.startMeasuringTimestamp = startDownloadingTime;
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
        this.key = webSiteDownloadInfoSnapshot.hashCode();
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

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
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

        private String speedTestIdentifier;
        private boolean succeed;
        private long startDownloadingTime;
        private String webSiteDownloadInfoSnapshot;

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

        public SpeedTestWebSiteDownloadInfoBuilder setStartDownloadingTimeSnapshot(long startDownloadingTime) {
            this.startDownloadingTime = startDownloadingTime;
            return this;
        }

        public SpeedTestWebSiteDownloadInfoBuilder setWebSiteDownloadInfoSnapshot(String webSiteDownloadInfoSnapshot) {
            this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
            return this;
        }

        public SpeedTestWebSite build() {
            return new SpeedTestWebSite(speedTestIdentifier, succeed, startDownloadingTime, webSiteDownloadInfoSnapshot);
        }

    }
}
