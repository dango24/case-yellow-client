package caseyellow.client.domain.system;

import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.UserInterruptException;

import java.io.File;
import java.net.URL;

/**
 * Created by Dan on 6/24/2017.
 */
public interface URLToFileService {
    long copyURLToFile(URL source, File destination) throws FileDownloadInfoException, UserInterruptException;
}
