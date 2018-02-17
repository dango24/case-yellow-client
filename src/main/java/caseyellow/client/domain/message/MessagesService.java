package caseyellow.client.domain.message;

/**
 * Created by Dan on 7/8/2017.
 */
public interface MessagesService {
    void testDone();
    void subTestStart();
    void showMessage(String message);
    void showDownloadFileProgress(long currentFileSize, long fullFileSize);
    void startDownloadingFile(String fileName);
    void finishDownloadingFile();
}
