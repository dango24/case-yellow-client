package caseyellow.client.domain.file.service;

import caseyellow.client.App;
import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.message.MessagesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class DownloadFileServiceImplTest {

    @Autowired
    private DownloadFileServiceImpl downloadFileService;

    @Autowired
    private MessagesService messagesService;

    @Test
    public void generateFileDownloadInfo() throws Exception {

        downloadFileService.setMessagesService(messagesService);
        downloadFileService.generateFileDownloadInfo(new FileDownloadProperties("vlc", "http://mirror.library.ucy.ac.cy/videolan/vlc/2.2.8/win32/vlc-2.2.8-win32.exe"));
    }
}