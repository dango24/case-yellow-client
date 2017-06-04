package app.test.entities;

import java.util.Date;

import static app.utils.Utils.format;
import static app.utils.Utils.round;

/**
 * Created by Dan on 04/10/2016.
 */
public class FileDownloadInfo {

    // Fields
    private String fileURL;
    private String fileName;
    private long   fileSizeInBytes;
    private double fileDownloadRateKBPerSec;
    private long   fileDownloadedTimeInMs;
    private Date   startDownloadingTime;

    //Constructors
    public FileDownloadInfo() {}

    public FileDownloadInfo(String url) {
        fileURL = url;
    }

    // Methods

    public double downloadRateKBperSec() {

        double fileDownloadTimeElapsedInSec = fileDownloadedTimeInMs /1000;
        double bytesPerSec = fileSizeInBytes /fileDownloadTimeElapsedInSec;

        return bytesPerSec / Math.pow(2, 10); // Transform to KB
    }

    public String getFileURL() {
        return fileURL;
    }

    public long getFileSizeInBytes() {
        return fileSizeInBytes;
    }

    public void setFileSizeInBytes(long fileSizeInBytes) {
        this.fileSizeInBytes = fileSizeInBytes;
    }

    public long getFileDownloadedTimeInMs() { return fileDownloadedTimeInMs; }

    public double getFileDownloadedTimeInSec() {
        return round( (double) fileDownloadedTimeInMs / 1000, 2);
    }

    public void setFileDownloadedTimeInMs(long fileDownloadedTimeInMs) {
        this.fileDownloadedTimeInMs = fileDownloadedTimeInMs;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getStartDownloadingTime() {
        return startDownloadingTime;
    }

    public void setStartDownloadingTime(Date startDownloadingTime) {
        this.startDownloadingTime = startDownloadingTime;
    }

    public double getFileDownloadRateKBPerSec() {
        return fileDownloadRateKBPerSec;
    }

    public void setFileDownloadRateKBPerSec(double fileDownloadRateKBPerSec) {
        this.fileDownloadRateKBPerSec = fileDownloadRateKBPerSec;
    }

    @Override
    public String toString() {
        return "FileDownloadInfo{" +
                "fileURL='" + fileURL + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSizeInBytes=" + fileSizeInBytes +
                ", fileDownloadedTimeInMs=" + fileDownloadedTimeInMs +
                ", startDownloadingTime=" + format(startDownloadingTime) +
                '}';
    }
}
