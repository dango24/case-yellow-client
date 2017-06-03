package speed.test.entities;

import utils.Utils;

import java.io.File;
import java.util.Date;

/**
 * Created by Dan on 12/10/2016.
 */
public class SpeedTestWebSiteDownloadInfo {

    private String speedTestIdentifier;
    private Date startMeasuringTime;
    private File webSiteDownloadInfoSnapshot;

    public SpeedTestWebSiteDownloadInfo(String speedTestIdentifier, Date startDownloadingTime, File webSiteDownloadInfoSnapshot) {
        this.speedTestIdentifier = speedTestIdentifier;
        this.startMeasuringTime = startDownloadingTime;
        this.webSiteDownloadInfoSnapshot = webSiteDownloadInfoSnapshot;
    }

    public String getSpeedTestIdentifier() {
        return speedTestIdentifier;
    }

    public Date getStartMeasuringTime() {
        return startMeasuringTime;
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
                ", startMeasuringTime=" + Utils.format(startMeasuringTime) +
                '}';
    }
}
