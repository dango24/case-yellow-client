package caseyellow.client.domain.message;

/**
 * Created by Dan on 7/8/2017.
 */
public interface MessagesService {
    void showMessage(String message);
    void subTestStart();
    void testDone();
}
