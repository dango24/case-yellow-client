package caseyellow.client.domain.file.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dan on 04/10/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileDownloadInfo {

    private String fileName;
    private String fileURL;
    private long   fileSizeInBytes;
    private double fileDownloadRateKBPerSec;
    private long   fileDownloadedDurationTimeInMs;
    private long   startDownloadingTimestamp;

    @Expose
    private boolean succeed;

    @Expose
    private String message;

    public FileDownloadInfo(String fileName, String url) {
        this.fileName = fileName;
        this.fileURL = url;
    }

    public FileDownloadInfo(FileDownloadInfoBuilder fileDownloadInfoBuilder) {
        fileName = fileDownloadInfoBuilder.fileName;
        fileURL = fileDownloadInfoBuilder.fileURL;
        fileSizeInBytes = fileDownloadInfoBuilder.fileSizeInBytes;
        fileDownloadRateKBPerSec = fileDownloadInfoBuilder.fileDownloadRateKBPerSec;
        fileDownloadedDurationTimeInMs = fileDownloadInfoBuilder.fileDownloadedDurationTimeInMs;
        startDownloadingTimestamp = fileDownloadInfoBuilder.startDownloadingTimestamp;
        succeed = fileDownloadInfoBuilder.succeed;
    }

    public static FileDownloadInfo errorFileDownloadInfo(String fileName, String url, String message) {
        FileDownloadInfo fileDownloadInfo = new FileDownloadInfo(fileName, url);
        fileDownloadInfo.setMessage(message);
        fileDownloadInfo.setSucceed(false);

        return fileDownloadInfo;
    }

    public static FileDownloadInfo emptyFileDownloadInfo() {
        return new FileDownloadInfoBuilder("test failed").addFileURL("noUrl")
                                                                 .addFileDownloadedDurationTimeInMs(1)
                                                                 .addFileSizeInBytes(1)
                                                                 .addFileDownloadRateKBPerSec(1)
                                                                 .addFileDownloadedDurationTimeInMs(1)
                                                                 .addStartDownloadingTime(1)
                                                                 .build();
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

        private boolean succeed;
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

        public FileDownloadInfoBuilder setSucceed() {
            this.succeed = true;
            return this;
        }

        public FileDownloadInfo build() {
            return new FileDownloadInfo(this);
        }

    }
}
