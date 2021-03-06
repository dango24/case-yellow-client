package caseyellow.client.domain.file.service;

import caseyellow.client.common.FileUtils;
import caseyellow.client.domain.file.model.DownloadedFileDetails;
import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.logger.services.CYLogger;
import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.system.SystemService;
import caseyellow.client.domain.system.URLToFileService;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.UserInterruptException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static caseyellow.client.common.FileUtils.createTmpDir;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class DownloadFileServiceImpl implements DownloadFileService {

    private static CYLogger logger = new CYLogger(DownloadFileServiceImpl.class);

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
    public FileDownloadInfo generateFileDownloadInfo(FileDownloadProperties fileDownloadProperties) throws UserInterruptException {
        return generateFileDownloadInfo(fileDownloadProperties, true);
    }

    @Override
    public FileDownloadInfo generateFileDownloadInfo(FileDownloadProperties fileDownloadProperties, boolean runTraceRoute) throws UserInterruptException {
        URL url;
        String md5;
        File tmpFile = null;
        long fileSizeInBytes;
        long startDownloadingTime;
        DownloadedFileDetails downloadedFileDetails;
        double fileDownloadRateKBPerSec;

        displayMessage("Downloading file: " + fileDownloadProperties.getIdentifier() + ", from url: " + fileDownloadProperties.getUrl());

        try {
            url = new URL(fileDownloadProperties.getUrl());
            tmpFile = new File(createTmpDir(), fileDownloadProperties.getIdentifier());

            startDownloadingTime = System.currentTimeMillis();
            downloadedFileDetails =
                    urlToFileService.copyURLToFile(fileDownloadProperties.getIdentifier(), url, tmpFile,
                                                   fileDownloadProperties.getSize(), runTraceRoute, fileDownloadProperties.getTimeoutInMin());
            fileSizeInBytes = tmpFile.length();
            md5 = systemService.convertToMD5(tmpFile);

            validateFile(fileDownloadProperties, fileSizeInBytes, md5);

            fileDownloadRateKBPerSec = calculateDownloadRateKBPerSec(downloadedFileDetails.getFileDownloadedDurationTimeInMs(), fileSizeInBytes);

            displayMessage(fileDownloadProperties.getIdentifier() + " finish download, rate: " + fileDownloadRateKBPerSec + "KB per sec");

            return new FileDownloadInfo.FileDownloadInfoBuilder(fileDownloadProperties.getIdentifier())
                                       .setSucceed()
                                       .addFileURL(url.toString())
                                       .addFileSizeInBytes(fileSizeInBytes)
                                       .addFileDownloadRateKBPerSec(fileDownloadRateKBPerSec)
                                       .addFileDownloadedDurationTimeInMs(downloadedFileDetails.getFileDownloadedDurationTimeInMs())
                                       .addStartDownloadingTime(startDownloadingTime)
                                       .addTraceRouteOutputPreviousDownloadFile(downloadedFileDetails.getTraceRouteOutputPreviousDownloadFile())
                                       .addTraceRouteOutputAfterDownloadFile(downloadedFileDetails.getTraceRouteOutputAfterDownloadFile())
                                       .addHeaders(downloadedFileDetails.getHeaders())
                                       .build();

        } catch (IOException | FileDownloadInfoException e) {
            String errorMessage = String.format("Failed to download file for identifier: %s, cause: %s", fileDownloadProperties.getIdentifier(), e.getMessage());
            logger.error(errorMessage, e);

            return FileDownloadInfo.errorFileDownloadInfo(fileDownloadProperties.getIdentifier(), fileDownloadProperties.getUrl(), errorMessage);

        } finally {
            FileUtils.deleteFile(tmpFile);
        }
    }

    private void validateFile(FileDownloadProperties fileDownloadProperties, long fileSizeInBytes, String md5) {
        if (fileDownloadProperties.getSize() != fileSizeInBytes || !fileDownloadProperties.getMd5().equals(md5)) {
            throw new FileDownloadInfoException(String.format("Invalidate file download info for file: %s, expected md5: %s, actual md5: %s; expected file size: %s, actual file size: %s", fileDownloadProperties.getIdentifier(), fileDownloadProperties.getMd5(), md5, fileDownloadProperties.getSize(), fileSizeInBytes));
        }
    }

    private double calculateDownloadRateKBPerSec(long fileDownloadedTimeInMs, long fileSizeInBytes) {

        double fileDownloadTimeElapsedInSec = fileDownloadedTimeInMs /1000.0;
        double bytesPerSec = fileSizeInBytes /fileDownloadTimeElapsedInSec;

        return bytesPerSec / Math.pow(2, 10); // Transform to KB
    }

    private void displayMessage(String message) {
        logger.info(message);
        messagesService.showMessage(message);
    }
}
