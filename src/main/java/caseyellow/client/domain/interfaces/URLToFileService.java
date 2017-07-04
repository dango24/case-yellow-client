package caseyellow.client.domain.interfaces;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Dan on 6/24/2017.
 */
public interface URLToFileService {
    void copyURLToFile(URL source, File destination) throws IOException;
}
