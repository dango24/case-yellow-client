package app.caseyellow.client.domain.services.interfaces;

import app.caseyellow.client.exceptions.FileDownloadInfoException;
import app.caseyellow.client.domain.model.test_entites.FileDownloadInfo;

/**
 * Created by dango on 6/3/17.
 */
public interface DownloadFileService {
    FileDownloadInfo generateFileDownloadInfo(String url) throws FileDownloadInfoException;
}
