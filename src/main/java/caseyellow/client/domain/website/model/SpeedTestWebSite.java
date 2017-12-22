package caseyellow.client.domain.website.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Dan on 12/10/2016.
 */
public class SpeedTestWebSite {

    @Expose
    private int key;

    @Expose
    private String webSiteDownloadInfoSnapshot;

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
                            String result) {

        this.succeed = succeed;
        this.speedTestIdentifier = speedTestIdentifier;
        this.startMeasuringTimestamp = startDownloadingTime;
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
        this.urlAddress = urlAddress;
        this.nonFlashResult = result;
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

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
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

        public SpeedTestWebSite build() {
            return new SpeedTestWebSite(speedTestIdentifier, succeed,
                                        startDownloadingTime, webSiteDownloadInfoSnapshot,
                                        urlAddress, nonFlashResult);
        }

    }
}
