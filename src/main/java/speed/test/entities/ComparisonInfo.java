package speed.test.entities;

/**
 * Created by Dan on 12/10/2016.
 */
public class ComparisonInfo {

    // Fields
    private ISPDownloadInfo  ispDownloadInfo;
    private FileDownloadInfo fileDownloadInfo;

    // Constructor
    public ComparisonInfo(ISPDownloadInfo ispDownloadInfo, FileDownloadInfo fileDownloadInfo) {
        this.ispDownloadInfo = ispDownloadInfo;
        this.fileDownloadInfo = fileDownloadInfo;
    }

    // Methods

    public ISPDownloadInfo getIspDownloadInfo() {
        return ispDownloadInfo;
    }

    public FileDownloadInfo getFileDownloadInfo() {
        return fileDownloadInfo;
    }

}
