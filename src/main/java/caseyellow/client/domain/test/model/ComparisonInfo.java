package caseyellow.client.domain.test.model;

import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import com.google.gson.annotations.Expose;

/**
 * Created by Dan on 12/10/2016.
 */
public class ComparisonInfo {

    @Expose
    private boolean success;

    private SpeedTestWebSite speedTestWebSite;
    private FileDownloadInfo fileDownloadInfo;

    public ComparisonInfo() {
    }

    public ComparisonInfo(SpeedTestWebSite speedTestWebSiteDownloadInfo, FileDownloadInfo fileDownloadInfo) {
        this.fileDownloadInfo = fileDownloadInfo;
        this.speedTestWebSite = speedTestWebSiteDownloadInfo;
        this.success = speedTestWebSite.isSucceed() && fileDownloadInfo.isSucceed();
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean failed() {
        return !success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SpeedTestWebSite getSpeedTestWebSite() {
        return speedTestWebSite;
    }

    public void setSpeedTestWebSite(SpeedTestWebSite speedTestWebSite) {
        this.speedTestWebSite = speedTestWebSite;
    }

    public FileDownloadInfo getFileDownloadInfo() {
        return fileDownloadInfo;
    }

    public void setFileDownloadInfo(FileDownloadInfo fileDownloadInfo) {
        this.fileDownloadInfo = fileDownloadInfo;
    }
}
