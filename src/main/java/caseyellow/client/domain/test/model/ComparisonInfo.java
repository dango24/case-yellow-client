package caseyellow.client.domain.test.model;

import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSiteDownloadInfo;

/**
 * Created by Dan on 12/10/2016.
 */
public class ComparisonInfo {

    private SpeedTestWebSiteDownloadInfo speedTestWebSiteDownloadInfo;
    private FileDownloadInfo fileDownloadInfo;

    public ComparisonInfo(SpeedTestWebSiteDownloadInfo speedTestWebSiteDownloadInfo, FileDownloadInfo fileDownloadInfo) {
        this.speedTestWebSiteDownloadInfo = speedTestWebSiteDownloadInfo;
        this.fileDownloadInfo = fileDownloadInfo;
    }

    public SpeedTestWebSiteDownloadInfo getSpeedTestWebSiteDownloadInfo() {
        return speedTestWebSiteDownloadInfo;
    }

    public FileDownloadInfo getFileDownloadInfo() {
        return fileDownloadInfo;
    }

}
