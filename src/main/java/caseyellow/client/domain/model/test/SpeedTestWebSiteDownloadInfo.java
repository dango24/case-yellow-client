package caseyellow.client.domain.model.test;

import java.io.File;
import java.util.Date;


/**
 * Created by Dan on 12/10/2016.
 */
public class SpeedTestWebSiteDownloadInfo {

    private String speedTestIdentifier;
    private long startMeasuringTimestamp;
    private transient File webSiteDownloadInfoSnapshot;

    public SpeedTestWebSiteDownloadInfo(String speedTestIdentifier, long startDownloadingTime, File webSiteDownloadInfoSnapshot) {
        this.speedTestIdentifier = speedTestIdentifier;
        this.startMeasuringTimestamp = startDownloadingTime;
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
    }

    public String getSpeedTestIdentifier() {
        return speedTestIdentifier;
    }

    public long getStartMeasuringTimestamp() {
        return startMeasuringTimestamp;
    }

    public File getWebSiteDownloadInfoSnapshot() {
        return webSiteDownloadInfoSnapshot;
    }

    public void setWebSiteDownloadInfoSnapshot(File webSiteDownloadInfoSnapshot) {
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
    }

    @Override
    public String toString() {
        return "SpeedTestWebSiteDownloadInfo{" +
                "speedTestIdentifier=" + speedTestIdentifier +
                ", startMeasuringTimestamp=" + startMeasuringTimestamp +
                '}';
    }
}
