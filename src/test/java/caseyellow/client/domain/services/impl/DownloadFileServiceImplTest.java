package caseyellow.client.domain.services.impl;

import caseyellow.client.common.Mapper;
import caseyellow.client.domain.file.model.FileDownloadInfo;
import caseyellow.client.domain.file.model.FileDownloadMetaData;
import caseyellow.client.domain.file.service.DownloadFileServiceImpl;
import caseyellow.client.domain.interfaces.MessagesService;
import caseyellow.client.domain.interfaces.SystemService;
import caseyellow.client.domain.interfaces.URLToFileService;
import caseyellow.client.infrastructre.SystemServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
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
    private final String FIREFOX_URL = "https://ftp.mozilla.org/pub/firefox/releases/37.0b1/win32/en-US/Firefox%20Setup%2037.0b1.exe";
    private final String FIREFOX = "firefox";
    private DownloadFileServiceImpl downloadFileService;

    @Before
    public void setUp() throws Exception {
        SystemService systemService = new SystemServiceImpl();
        Mapper mapper = mock(Mapper.class);
        MessagesService messagesService = mock(MessagesService.class);
        when(mapper.getFileNameFromUrl(FIREFOX_URL)).thenReturn(FIREFOX);
        downloadFileService = new DownloadFileServiceImpl();
        downloadFileService.setSystemService(systemService);
        downloadFileService.setMapper(mapper);
    }

    @Ignore
    @Test
    public void generateFileDownloadInfoDummy() throws Exception {
        URLToFileService urlToFileService = mock(URLToFileService.class);
        downloadFileService.setUrlToFileService(urlToFileService);

        FileDownloadInfo fileDownloadInfo = downloadFileService.generateFileDownloadInfo(new FileDownloadMetaData(FIREFOX, FIREFOX_URL));
        assertNotNull(fileDownloadInfo);
    }

}