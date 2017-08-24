package caseyellow.client.infrastructre;

import caseyellow.client.domain.interfaces.MessagesService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImp implements MessagesService {

    private MessagesService presentationMessagesService;

    public void setPresentationMessagesService(MessagesService presentationMessagesService) {
        this.presentationMessagesService = presentationMessagesService;
    }

    @Override
    public void showMessage(String message) {
        presentationMessagesService.showMessage(message);
    }
}
