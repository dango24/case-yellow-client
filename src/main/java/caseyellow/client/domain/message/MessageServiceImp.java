package caseyellow.client.domain.message;

import org.springframework.stereotype.Service;

@Service("messageServiceImp")
public class MessageServiceImp implements MessagesService {

    private MessagesService presentationMessagesService;

    public void setPresentationMessagesService(MessagesService presentationMessagesService) {
        this.presentationMessagesService = presentationMessagesService;
    }

    @Override
    public void showMessage(String message) {
        presentationMessagesService.showMessage(message);
    }

    @Override
    public void showDownloadFileProgress(long currentFileSize, long fullFileSize) {
        presentationMessagesService.showDownloadFileProgress(currentFileSize, fullFileSize);
    }

    @Override
    public void startDownloadingFile(String fileName) {
        presentationMessagesService.startDownloadingFile(fileName);
    }

    @Override
    public void finishDownloadingFile() {
        presentationMessagesService.finishDownloadingFile();
    }

    @Override
    public void subTestStart() {
        presentationMessagesService.subTestStart();
    }

    @Override
    public void testDone() {
        presentationMessagesService.testDone();
    }
}
