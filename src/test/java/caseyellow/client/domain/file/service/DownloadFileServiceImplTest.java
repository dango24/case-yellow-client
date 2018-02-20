package caseyellow.client.domain.file.service;

import caseyellow.client.App;
import caseyellow.client.domain.file.model.FileDownloadProperties;
import caseyellow.client.domain.message.MessageServiceImp;
import caseyellow.client.domain.message.MessagesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class DownloadFileServiceImplTest {

    @Autowired
    private DownloadFileServiceImpl downloadFileService;

    @Autowired
    @Qualifier("messageServiceImp")
    private MessageServiceImp messagesService;

    @Test
    public void generateFileDownloadInfo() throws Exception {

        MessagesService presentationMessageService = mock(MessagesService.class);
        messagesService.setPresentationMessagesService(presentationMessageService);
        downloadFileService.setMessagesService(messagesService);
        downloadFileService.generateFileDownloadInfo(new FileDownloadProperties("amazon-workSpace", "https://d2td7dqidlhjx7.cloudfront.net/prod/global/windows/Amazon+WorkSpaces.msi"));
    }
}