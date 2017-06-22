package app.caseyellow.client.domain.services.impl;

import app.caseyellow.client.common.Utils;
import app.caseyellow.client.domain.model.test_entites.FileDownloadInfo;
import app.caseyellow.client.domain.services.DownloadFileService;
import app.caseyellow.client.exceptions.FileDownloadInfoException;
import app.caseyellow.client.common.UrlMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class DownloadFileManager implements DownloadFileService {

    @Autowired
    private UrlMapper urlMapper;

    // Constants
    private final static String tmpDirPath = System.getProperty("java.io.tmpdir");

    @Override
    public FileDownloadInfo generateFileDownloadInfo(String urlStr) throws FileDownloadInfoException {
        URL url;
        File tmpFile;
        long startDownloadingTime;
        long endDownloadingTime;
        long fileSizeInBytes;
        long fileDownloadedTimeInMs;
        double fileDownloadRateKBPerSec;
        String startDownloadingDate;
        String fileName;

        try {

            fileName = urlMapper.getFileNameFromUrl(urlStr);
            url = new URL(urlStr);
            tmpFile = new File(tmpDirPath, Utils.generateUniqueID());

            startDownloadingTime = System.currentTimeMillis();
            FileUtils.copyURLToFile(url, tmpFile);
            endDownloadingTime = System.currentTimeMillis();

            fileDownloadedTimeInMs = (endDownloadingTime - startDownloadingTime);
            fileSizeInBytes = tmpFile.length();
            fileDownloadRateKBPerSec = calculateDownloadRateKBPerSec(fileDownloadedTimeInMs, fileSizeInBytes);
            startDownloadingDate = Utils.format(new Date(startDownloadingTime));

            FileUtils.deleteDirectory(tmpFile);

            return new FileDownloadInfo.FileDownloadInfoBuilder(fileName)
                                       .addFileURL(url.toString())
                                       .addFileSizeInBytes(fileSizeInBytes)
                                       .addFileDownloadRateKBPerSec(fileDownloadRateKBPerSec)
                                       .addFileDownloadedTimeInMs(fileDownloadedTimeInMs)
                                       .addStartDownloadingTime(startDownloadingDate)
                                       .build();

        } catch (IOException e) {
            throw new FileDownloadInfoException(e.getMessage());
        }
    }

    public double calculateDownloadRateKBPerSec(long fileDownloadedTimeInMs, long fileSizeInBytes) {

        double fileDownloadTimeElapsedInSec = fileDownloadedTimeInMs /1000;
        double bytesPerSec = fileSizeInBytes /fileDownloadTimeElapsedInSec;

        return bytesPerSec / Math.pow(2, 10); // Transform to KB
    }
}
