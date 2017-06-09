package app.test.web.site.services;

import app.exceptions.FileDownloadInfoException;
import app.test.entities.FileDownloadInfo;

/**
 * Created by dango on 6/3/17.
 */
public interface DownloadFileService {
    FileDownloadInfo generateFileDownloadInfo(String url) throws FileDownloadInfoException;
}
