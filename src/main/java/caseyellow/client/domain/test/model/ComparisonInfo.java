package caseyellow.client.domain.test.model;

import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dan on 12/10/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComparisonInfo {

    @Expose
    private boolean success;

    private SpeedTestWebSite speedTestWebSite;
    private FileDownloadInfo fileDownloadInfo;

    public ComparisonInfo(SpeedTestWebSite speedTestWebSiteDownloadInfo, FileDownloadInfo fileDownloadInfo) {
        this.fileDownloadInfo = fileDownloadInfo;
        this.speedTestWebSite = speedTestWebSiteDownloadInfo;
        this.success = speedTestWebSite.isSucceed() && fileDownloadInfo.isSucceed();
    }

    public boolean failed() {
        return !success;
    }
}
