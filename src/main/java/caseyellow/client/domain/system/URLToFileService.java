package caseyellow.client.domain.system;

import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.UserInterruptException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 6/24/2017.
 */
public interface URLToFileService {
    Pair<Long, Map<String, List<String>>> copyURLToFile(String fileName, URL source, File destination, long fileSize) throws FileDownloadInfoException, UserInterruptException;
}
