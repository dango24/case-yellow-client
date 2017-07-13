package caseyellow.client.domain.file.service;

import caseyellow.client.common.Mapper;
import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.domain.interfaces.SystemService;
import caseyellow.client.domain.interfaces.URLToFileService;
import caseyellow.client.exceptions.FileDownloadInfoException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static caseyellow.client.common.Utils.createTmpDir;

/**
 * Created by dango on 6/3/17.
 */
@Service
@Profile("beta")
public class DownloadFileServiceImpl implements DownloadFileService {

    // Logger
    private Logger logger = Logger.getLogger(DownloadFileServiceImpl.class);

    // Fields
    private Mapper mapper;
    private SystemService systemService;
    private URLToFileService urlToFileService;
    private MessagesService messagesService;

    // Setters

    @Autowired
    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

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
        long fileSizeInBytes;
        long fileDownloadedDurationTimeInMs;
        double fileDownloadRateKBPerSec;
        String fileName;

        try {
            fileName = mapper.getFileNameFromUrl(urlStr);
            url = new URL(urlStr);
            tmpFile = new File(createTmpDir(), fileName);

            String message = "Start measuring and downloading file: " + fileName + ", from url: " + urlStr;
            logger.info(message);
            messagesService.showMessage(message);

            long startDownloadingTime = System.currentTimeMillis();
            urlToFileService.copyURLToFile(url, tmpFile);
            fileDownloadedDurationTimeInMs = System.currentTimeMillis() - startDownloadingTime;

            fileSizeInBytes = tmpFile.length();
            fileDownloadRateKBPerSec = calculateDownloadRateKBPerSec(fileDownloadedDurationTimeInMs, fileSizeInBytes);

            systemService.deleteDirectory(tmpFile.getParentFile());

            return new FileDownloadInfo.FileDownloadInfoBuilder(fileName)
                                       .addFileURL(url.toString())
                                       .addFileSizeInBytes(fileSizeInBytes)
                                       .addFileDownloadRateKBPerSec(fileDownloadRateKBPerSec)
                                       .addFileDownloadedDurationTimeInMs(fileDownloadedDurationTimeInMs)
                                       .addStartDownloadingTime(startDownloadingTime)
                                       .build();
        } catch (IOException e) {
            messagesService.showMessage("Failed to download file, " + e.getMessage());
            logger.error("Failed to download file, " + e.getMessage(), e);
            throw new FileDownloadInfoException(e.getMessage());
        }
    }

    private double calculateDownloadRateKBPerSec(long fileDownloadedTimeInMs, long fileSizeInBytes) {

        double fileDownloadTimeElapsedInSec = fileDownloadedTimeInMs /1000;
        double bytesPerSec = fileSizeInBytes /fileDownloadTimeElapsedInSec;

        return bytesPerSec / Math.pow(2, 10); // Transform to KB
    }
}
