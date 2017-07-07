package caseyellow.client.domain.file.service;

import caseyellow.client.domain.interfaces.URLToFileService;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Dan on 7/7/2017.
 */
@Service
@Profile("test")
public class URLToFileServiceStub implements URLToFileService  {
    @Override
    public void copyURLToFile(URL source, File destination) throws IOException {
        // do nothing
    }
}
