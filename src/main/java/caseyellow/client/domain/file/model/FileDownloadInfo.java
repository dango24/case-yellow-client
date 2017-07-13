package caseyellow.client.domain.file.model;

/**
 * Created by Dan on 04/10/2016.
 */
public class FileDownloadInfo {

    // Fields
    private String fileName;
    private String fileURL;
    private long   fileSizeInBytes;
    private double fileDownloadRateKBPerSec;
    private long   fileDownloadedDurationTimeInMs;
    private long   startDownloadingTimestamp;

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
        fileDownloadedDurationTimeInMs = fileDownloadInfoBuilder.fileDownloadedDurationTimeInMs;
        startDownloadingTimestamp = fileDownloadInfoBuilder.startDownloadingTimestamp;
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

    public long getFileDownloadedDurationTimeInMs() { return fileDownloadedDurationTimeInMs; }

    public void setFileDownloadedDurationTimeInMs(long fileDownloadedDurationTimeInMs) {
        this.fileDownloadedDurationTimeInMs = fileDownloadedDurationTimeInMs;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getStartDownloadingTimestamp() {
        return startDownloadingTimestamp;
    }

    public void setStartDownloadingTimestamp(long startDownloadingTimestamp) {
        this.startDownloadingTimestamp = startDownloadingTimestamp;
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
                ", fileDownloadedDurationTimeInMs=" + fileDownloadedDurationTimeInMs +
                ", startDownloadingTimestamp=" + startDownloadingTimestamp +
                '}';
    }

    public static class FileDownloadInfoBuilder {

        // Fields
        private String fileURL;
        private String fileName;
        private long   fileSizeInBytes;
        private double fileDownloadRateKBPerSec;
        private long fileDownloadedDurationTimeInMs;
        private long startDownloadingTimestamp;

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

        public FileDownloadInfoBuilder addFileDownloadedDurationTimeInMs(long fileDownloadedTimeInMs) {
            this.fileDownloadedDurationTimeInMs = fileDownloadedTimeInMs;
            return this;
        }

        public FileDownloadInfoBuilder addStartDownloadingTime(long startDownloadingTime) {
            this.startDownloadingTimestamp = startDownloadingTime;
            return this;
        }

        public FileDownloadInfo build() {
            return new FileDownloadInfo(this);
        }
    }
}
