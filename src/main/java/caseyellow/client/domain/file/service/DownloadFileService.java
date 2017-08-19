package caseyellow.client.domain.file.service;

import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.domain.file.model.FileDownloadInfo;

import java.io.Closeable;

/**
 * Created by dango on 6/3/17.
 */
public interface DownloadFileService extends Closeable {
    FileDownloadInfo generateFileDownloadInfo(String url) throws FileDownloadInfoException;
}
