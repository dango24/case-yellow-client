package caseyellow.client.infrastructre;

import caseyellow.client.domain.services.interfaces.URLToFileService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Dan on 6/24/2017.
 */
public class URLToFileServiceImpl implements URLToFileService {

    @Override
    public void copyURLToFile(URL source, File destination) throws IOException {
        FileUtils.copyURLToFile(source, destination);
    }
}
