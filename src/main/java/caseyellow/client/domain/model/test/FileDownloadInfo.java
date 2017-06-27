package caseyellow.client.domain.model.test;

import java.util.Date;

/**
 * Created by Dan on 04/10/2016.
 */
public class FileDownloadInfo {

    // Fields
    private String fileName;
    private String fileURL;
    private long   fileSizeInBytes;
    private double fileDownloadRateKBPerSec;
    private long   fileDownloadedTimeInMs;
    private Date   startDownloadingTime;

    //Constructors
    public FileDownloadInfo() {}

    public FileDownloadInfo(String url) {
        fileURL = url;
    }

    public FileDownloadInfo(FileDownloadInfoBuilder fileDownloadInfoBuilder) {
        fileName = fileDownloadInfoBuilder.fileName;
        fileURL = fileDownloadInfoBuilder.fileURL;
        fileSizeInBytes = fileDownloadInfoBuilder.fileSizeInBytes;
        fileDownloadRateKBPerSec = fileDownloadInfoBuilder.fileDownloadRateKBPerSec;
        fileDownloadedTimeInMs = fileDownloadInfoBuilder.fileDownloadedTimeInMs;
        startDownloadingTime = fileDownloadInfoBuilder.startDownloadingTime;
    }

    // Methods

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
                ", startDownloadingTime=" + startDownloadingTime +
                '}';
    }

    public static class FileDownloadInfoBuilder {

        // Fields
        private String fileURL;
        private String fileName;
        private long   fileSizeInBytes;
        private double fileDownloadRateKBPerSec;
        private long   fileDownloadedTimeInMs;
        private Date startDownloadingTime;

        public FileDownloadInfoBuilder(String fileName) {
            this.fileName = fileName;
        }

        public FileDownloadInfoBuilder addFileURL(String fileURL) {
            this.fileURL = fileURL;
            return this;
        }

        public FileDownloadInfoBuilder addFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public FileDownloadInfoBuilder addFileSizeInBytes(long fileSizeInBytes) {
            this.fileSizeInBytes = fileSizeInBytes;
            return this;
        }

        public FileDownloadInfoBuilder addFileDownloadRateKBPerSec(double fileDownloadRateKBPerSec) {
            this.fileDownloadRateKBPerSec = fileDownloadRateKBPerSec;
            return this;
        }

        public FileDownloadInfoBuilder addFileDownloadedTimeInMs(long fileDownloadedTimeInMs) {
            this.fileDownloadedTimeInMs = fileDownloadedTimeInMs;
            return this;
        }

        public FileDownloadInfoBuilder addStartDownloadingTime(Date startDownloadingTime) {
            this.startDownloadingTime = startDownloadingTime;
            return this;
        }

        public FileDownloadInfo build() {
            return new FileDownloadInfo(this);
        }
    }
}
