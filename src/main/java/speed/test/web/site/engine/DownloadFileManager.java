package speed.test.web.site.engine;

import speed.test.entities.FileDownloadInfo;
import speed.test.web.site.services.DownloadFileService;

/**
 * Created by dango on 6/3/17.
 */
public class DownloadFileManager implements DownloadFileService {

    @Override
    public FileDownloadInfo generateFileDownloadInfo(String url) {
        System.out.println("generateFileDownloadInfo " + this.getClass().getName());
        return null;
    }
}
