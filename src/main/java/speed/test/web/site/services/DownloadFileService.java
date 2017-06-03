package speed.test.web.site.services;

import speed.test.entities.FileDownloadInfo;

/**
 * Created by dango on 6/3/17.
 */
public interface DownloadFileService {
    FileDownloadInfo generateFileDownloadInfo(String url);
}
