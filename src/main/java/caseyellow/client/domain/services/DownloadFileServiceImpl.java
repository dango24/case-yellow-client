package caseyellow.client.domain.services;

import caseyellow.client.common.Mapper;
import caseyellow.client.domain.model.test.FileDownloadInfo;
import caseyellow.client.domain.services.interfaces.DownloadFileService;
import caseyellow.client.domain.services.interfaces.SystemService;
import caseyellow.client.domain.services.interfaces.URLToFileService;
import caseyellow.client.exceptions.FileDownloadInfoException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import static caseyellow.client.common.Utils.generateUniqueID;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class DownloadFileServiceImpl implements DownloadFileService {

    // Logger
    private Logger logger = Logger.getLogger(DownloadFileServiceImpl.class);

    // Constants
    private final static String tmpDirPath = System.getProperty("java.io.tmpdir");

    // Fields
    private Mapper mapper;
    private SystemService systemService;
    private URLToFileService urlToFileService;

    // Setters

    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setUrlToFileService(URLToFileService urlToFileService) {
        this.urlToFileService = urlToFileService;
    }

    @Autowired
    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    // Methods

    @Override
    public FileDownloadInfo generateFileDownloadInfo(String urlStr) throws FileDownloadInfoException {
        URL url;
        File tmpFile;
        long startDownloadingTime;
        long endDownloadingTime;
        long fileSizeInBytes;
        long fileDownloadedTimeInMs;
        double fileDownloadRateKBPerSec;
        String fileName;

        try {
            fileName = mapper.getFileNameFromUrl(urlStr);
            url = new URL(urlStr);
            tmpFile = new File(createTmpDir(), fileName);

            logger.debug("Start measuring and downloading file: " + fileName + ", from url: " + urlStr);
            startDownloadingTime = System.currentTimeMillis();
            urlToFileService.copyURLToFile(url, tmpFile);
            endDownloadingTime = System.currentTimeMillis();

            fileDownloadedTimeInMs = (endDownloadingTime - startDownloadingTime);
            fileSizeInBytes = tmpFile.length();
            fileDownloadRateKBPerSec = calculateDownloadRateKBPerSec(fileDownloadedTimeInMs, fileSizeInBytes);

            systemService.deleteDirectory(tmpFile.getParentFile());

            return new FileDownloadInfo.FileDownloadInfoBuilder(fileName)
                                       .addFileURL(url.toString())
                                       .addFileSizeInBytes(fileSizeInBytes)
                                       .addFileDownloadRateKBPerSec(fileDownloadRateKBPerSec)
                                       .addFileDownloadedTimeInMs(fileDownloadedTimeInMs)
                                       .addStartDownloadingTime(startDownloadingTime)
                                       .build();
        } catch (IOException e) {
            throw new FileDownloadInfoException(e.getMessage());
        }
    }

    private File createTmpDir() {
        File tmpDir = new File(tmpDirPath, generateUniqueID());
        tmpDir.mkdir();

        return tmpDir;
    }

    private double calculateDownloadRateKBPerSec(long fileDownloadedTimeInMs, long fileSizeInBytes) {

        double fileDownloadTimeElapsedInSec = fileDownloadedTimeInMs /1000;
        double bytesPerSec = fileSizeInBytes /fileDownloadTimeElapsedInSec;

        return bytesPerSec / Math.pow(2, 10); // Transform to KB
    }
}
