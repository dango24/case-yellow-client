package speed.test.entities;

import utils.Utils;
import java.util.Date;

/**
 * Created by Dan on 12/10/2016.
 */
public class ISPDownloadInfo{

    private String speedTestWebSite;
    private double downloadSpeedRateMb;
    private Date startMeasuringTime;

    public ISPDownloadInfo(String isp, double downloadSpeedRate, Date startDownloadingTime) {
        this.speedTestWebSite = isp;
        this.downloadSpeedRateMb = downloadSpeedRate;
        this.startMeasuringTime = startDownloadingTime;
    }

    public String getSpeedTestWebSite() {
        return speedTestWebSite;
    }

    public double getDownloadSpeedRateMb() {
        return downloadSpeedRateMb;
    }

    public double getDownloadRateInKB() {
        return (downloadSpeedRateMb /8.0) * Math.pow(2,10);
    }

    public Date getStartMeasuringTime() {
        return startMeasuringTime;
    }

    @Override
    public String toString() {
        return "ISPDownloadInfo{" +
                "speedTestWebSite=" + speedTestWebSite +
                ", downloadSpeedRateMb=" + downloadSpeedRateMb +
                ", startMeasuringTime=" + Utils.format(startMeasuringTime) +
                '}';
    }
}
