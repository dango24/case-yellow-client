package caseyellow.client.domain.services.impl;

import caseyellow.client.common.Mapper;
import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.file.service.DownloadFileServiceImpl;
import caseyellow.client.domain.interfaces.SystemService;
import caseyellow.client.domain.interfaces.URLToFileService;
import caseyellow.client.infrastructre.SystemServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Dan on 6/24/2017.
 */
public class DownloadFileServiceImplTest {

    // Constants
    private final String KODI_URL = "http://mirrors.kodi.tv/releases/osx/x86_64/kodi-17.3-Krypton-x86_64.dmg";
    private DownloadFileServiceImpl downloadFileService;

    @Before
    public void setUp() throws Exception {
        SystemService systemService = new SystemServiceImpl();
        Mapper mapper = mock(Mapper.class);
        when(mapper.getFileNameFromUrl(KODI_URL)).thenReturn("kodi");
        downloadFileService = new DownloadFileServiceImpl();
        downloadFileService.setSystemService(systemService);
        downloadFileService.setMapper(mapper);
    }

    @Test
    public void generateFileDownloadInfoDummy() throws Exception {
        URLToFileService urlToFileService = mock(URLToFileService.class);
        downloadFileService.setUrlToFileService(urlToFileService);

        FileDownloadInfo fileDownloadInfo = downloadFileService.generateFileDownloadInfo(KODI_URL);
        assertNotNull(fileDownloadInfo);
    }

    @Test
    public void generateFileDownloadInfoKodi() throws Exception {
        URLToFileService urlToFileService = new URLToFileServiceImpl();
        downloadFileService.setUrlToFileService(urlToFileService);

        FileDownloadInfo fileDownloadInfo = downloadFileService.generateFileDownloadInfo(KODI_URL);
        assertNotNull(fileDownloadInfo);
        assertNotNull(fileDownloadInfo.getFileName());
        assertNotNull(fileDownloadInfo.getFileURL());
        assertTrue(fileDownloadInfo.getFileDownloadedTimeInMs() > 0);
        assertTrue(fileDownloadInfo.getFileDownloadRateKBPerSec() > 0);
        assertTrue(fileDownloadInfo.getFileSizeInBytes() > 0);
        assertTrue(fileDownloadInfo.getStartDownloadingTimestamp() < System.currentTimeMillis());
    }

}