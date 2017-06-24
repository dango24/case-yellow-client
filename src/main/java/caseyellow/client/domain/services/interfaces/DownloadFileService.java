package caseyellow.client.domain.services.interfaces;

import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.domain.model.test.FileDownloadInfo;

/**
 * Created by dango on 6/3/17.
 */
public interface DownloadFileService {
    FileDownloadInfo generateFileDownloadInfo(String url) throws FileDownloadInfoException;
}
