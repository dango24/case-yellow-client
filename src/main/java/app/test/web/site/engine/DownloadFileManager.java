package app.test.web.site.engine;

import org.springframework.stereotype.Service;
import app.test.entities.FileDownloadInfo;
import app.test.web.site.services.DownloadFileService;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class DownloadFileManager implements DownloadFileService {

    @Override
    public FileDownloadInfo generateFileDownloadInfo(String url) {
        System.out.println("generateFileDownloadInfo " + this.getClass().getName());
        return null;
    }
}
