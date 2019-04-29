package caseyellow.client.domain.system;

import caseyellow.client.domain.file.model.DownloadedFileDetails;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.UserInterruptException;

import java.io.File;
import java.net.URL;

/**
 * Created by Dan on 6/24/2017.
 */
public interface URLToFileService {
    DownloadedFileDetails copyURLToFile(String fileName,
                                        URL source,
                                        File destination,
                                        long fileSize,
                                        boolean runTraceRoute,
                                        int timeoutInMin) throws FileDownloadInfoException, UserInterruptException;
}
