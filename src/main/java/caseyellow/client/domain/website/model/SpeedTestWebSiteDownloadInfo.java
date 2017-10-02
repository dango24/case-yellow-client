package caseyellow.client.domain.website.model;



/**
 * Created by Dan on 12/10/2016.
 */
public class SpeedTestWebSiteDownloadInfo {

    private boolean succeed;
    private String speedTestIdentifier;
    private long startMeasuringTimestamp;
    private String webSiteDownloadInfoSnapshot;

    public SpeedTestWebSiteDownloadInfo(String speedTestIdentifier, boolean succeed, long startDownloadingTime, String webSiteDownloadInfoSnapshot) {
        this.succeed = succeed;
        this.speedTestIdentifier = speedTestIdentifier;
        this.startMeasuringTimestamp = startDownloadingTime;
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
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

    @Override
    public String toString() {
        return "SpeedTestWebSiteDownloadInfo{" +
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

        public SpeedTestWebSiteDownloadInfo build() {
            return new SpeedTestWebSiteDownloadInfo(speedTestIdentifier, succeed, startDownloadingTime, webSiteDownloadInfoSnapshot);
        }

    }
}
