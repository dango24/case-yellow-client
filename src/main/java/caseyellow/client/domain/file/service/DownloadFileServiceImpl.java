package caseyellow.client.domain.file.service;

import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.system.SystemService;
import caseyellow.client.domain.system.URLToFileService;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.UserInterruptException;
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
@Profile({"prod", "integration"})
public class DownloadFileServiceImpl implements DownloadFileService {

    private Logger logger = Logger.getLogger(DownloadFileServiceImpl.class);

    private SystemService systemService;
    private URLToFileService urlToFileService;
    private MessagesService messagesService;

    @Autowired
    public void setMessagesService(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Autowired
    public void setUrlToFileService(URLToFileService urlToFileService) {
        this.urlToFileService = urlToFileService;
    }

    @Autowired
    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }


    @Override
    public FileDownloadInfo generateFileDownloadInfo(FileDownloadMetaData fileDownloadMetaData) throws UserInterruptException {
        URL url;
        File tmpFile;
        long fileSizeInBytes;
        long startDownloadingTime;
        long fileDownloadedDurationTimeInMs;
        double fileDownloadRateKBPerSec;

        try {
            url = new URL(fileDownloadMetaData.getFileURL());
            tmpFile = new File(createTmpDir(), fileDownloadMetaData.getFileName());

            String message = "Downloading file: " + fileDownloadMetaData.getFileName() + ", from url: " + fileDownloadMetaData.getFileURL();
            logger.info(message);
            messagesService.showMessage(message);

            startDownloadingTime = System.currentTimeMillis();
            fileDownloadedDurationTimeInMs = urlToFileService.copyURLToFile(url, tmpFile);
            fileSizeInBytes = tmpFile.length();
            fileDownloadRateKBPerSec = calculateDownloadRateKBPerSec(fileDownloadedDurationTimeInMs, fileSizeInBytes);

            systemService.deleteDirectory(tmpFile.getParentFile());
            messagesService.showMessage(fileDownloadMetaData.getFileName() + " finish download, rate: " + fileDownloadRateKBPerSec + "KB per sec");

            return new FileDownloadInfo.FileDownloadInfoBuilder(fileDownloadMetaData.getFileName())
                                       .addSucceed()
                                       .addFileURL(url.toString())
                                       .addFileSizeInBytes(fileSizeInBytes)
                                       .addFileDownloadRateKBPerSec(fileDownloadRateKBPerSec)
                                       .addFileDownloadedDurationTimeInMs(fileDownloadedDurationTimeInMs)
                                       .addStartDownloadingTime(startDownloadingTime)
                                       .build();

        } catch (IOException | FileDownloadInfoException e) {
            logger.error("Failed to download file, " + e.getMessage(), e);
            return FileDownloadInfo.errorFileDownloadInfo(fileDownloadMetaData.getFileURL(), e.getMessage());
        }
    }

    private double calculateDownloadRateKBPerSec(long fileDownloadedTimeInMs, long fileSizeInBytes) {

        double fileDownloadTimeElapsedInSec = fileDownloadedTimeInMs /1000;
        double bytesPerSec = fileSizeInBytes /fileDownloadTimeElapsedInSec;

        return bytesPerSec / Math.pow(2, 10); // Transform to KB
    }

    @Override
    public void close() throws IOException {
        urlToFileService.close();
    }
}
