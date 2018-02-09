package caseyellow.client.domain.file.service;

import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.message.MessagesService;
import caseyellow.client.domain.system.SystemService;
import caseyellow.client.domain.system.URLToFileService;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.UserInterruptException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import static caseyellow.client.common.Utils.convertToMD5;
import static caseyellow.client.common.Utils.createTmpDir;
import static java.util.Objects.nonNull;

/**
 * Created by dango on 6/3/17.
 */
@Service
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
    public FileDownloadInfo generateFileDownloadInfo(FileDownloadProperties fileDownloadProperties) throws UserInterruptException {
        URL url;
        String md5;
        File tmpFile = null;
        long fileSizeInBytes;
        long startDownloadingTime;
        long fileDownloadedDurationTimeInMs;
        double fileDownloadRateKBPerSec;

        try {
            url = new URL(fileDownloadProperties.getUrl());
            tmpFile = new File(createTmpDir(), fileDownloadProperties.getIdentifier());

            String message = "Downloading file: " + fileDownloadProperties.getIdentifier() + ", from url: " + fileDownloadProperties.getUrl();
            logger.info(message);
            messagesService.showMessage(message);

            startDownloadingTime = System.currentTimeMillis();
            fileDownloadedDurationTimeInMs = urlToFileService.copyURLToFile(fileDownloadProperties.getIdentifier(), url, tmpFile, fileDownloadProperties.getSize());
            fileSizeInBytes = tmpFile.length();
            md5 = convertToMD5(tmpFile);

            validateFile(fileDownloadProperties, fileSizeInBytes, md5);

            fileDownloadRateKBPerSec = calculateDownloadRateKBPerSec(fileDownloadedDurationTimeInMs, fileSizeInBytes);

            messagesService.showMessage(fileDownloadProperties.getIdentifier() + " finish download, rate: " + fileDownloadRateKBPerSec + "KB per sec");

            return new FileDownloadInfo.FileDownloadInfoBuilder(fileDownloadProperties.getIdentifier())
                                       .addSucceed()
                                       .addFileURL(url.toString())
                                       .addFileSizeInBytes(fileSizeInBytes)
                                       .addFileDownloadRateKBPerSec(fileDownloadRateKBPerSec)
                                       .addFileDownloadedDurationTimeInMs(fileDownloadedDurationTimeInMs)
                                       .addStartDownloadingTime(startDownloadingTime)
                                       .build();

        } catch (IOException | FileDownloadInfoException | NoSuchAlgorithmException e) {
            logger.error("Failed to download file, " + e.getMessage(), e);
            return FileDownloadInfo.errorFileDownloadInfo(fileDownloadProperties.getUrl(), e.getMessage());

        } finally {
            if (nonNull(tmpFile)) {
                systemService.deleteDirectory(tmpFile.getParentFile());
            }
        }
    }

    private void validateFile(FileDownloadProperties fileDownloadProperties, long fileSizeInBytes, String md5) {
        if (fileDownloadProperties.getSize() != fileSizeInBytes || !fileDownloadProperties.getMd5().equals(md5)) {
            throw new FileDownloadInfoException(String.format("Invalidate file download info, expected md5: %s, actual md5: %s; expected file size: %s, actual file size: %s",fileDownloadProperties.getMd5(), md5, fileDownloadProperties.getSize(), fileSizeInBytes));
        }
    }

    private double calculateDownloadRateKBPerSec(long fileDownloadedTimeInMs, long fileSizeInBytes) {

        double fileDownloadTimeElapsedInSec = fileDownloadedTimeInMs /1000;
        double bytesPerSec = fileSizeInBytes /fileDownloadTimeElapsedInSec;

        return bytesPerSec / Math.pow(2, 10); // Transform to KB
    }

}
