package caseyellow.client.domain.file.service;

import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.domain.file.model.FileDownloadInfo;

/**
 * Created by dango on 6/3/17.
 */
public interface DownloadFileService {
    FileDownloadInfo generateFileDownloadInfo(FileDownloadProperties fileDownloadProperties, boolean runTraceRoute) throws FileDownloadInfoException;
}
