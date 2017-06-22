package app.caseyellow.client.domain.model.test_entites;

/**
 * Created by Dan on 12/10/2016.
 */
public class ComparisonInfo {

    // Fields
    private SpeedTestWebSiteDownloadInfo speedTestWebSiteDownloadInfo;
    private FileDownloadInfo fileDownloadInfo;

    // Constructor
    public ComparisonInfo(SpeedTestWebSiteDownloadInfo speedTestWebSiteDownloadInfo, FileDownloadInfo fileDownloadInfo) {
        this.speedTestWebSiteDownloadInfo = speedTestWebSiteDownloadInfo;
        this.fileDownloadInfo = fileDownloadInfo;
    }

    // Methods

    public SpeedTestWebSiteDownloadInfo getSpeedTestWebSiteDownloadInfo() {
        return speedTestWebSiteDownloadInfo;
    }

    public FileDownloadInfo getFileDownloadInfo() {
        return fileDownloadInfo;
    }

}
