package caseyellow.client.presentation;

import caseyellow.client.domain.interfaces.MessagesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class MainFormTest {

    private MainForm messagesService;

    @Before
    public void init() {
        this.messagesService = new MainForm();
        messagesService.view();
    }

    @Test
    public void showMessage() throws Exception {
        SwingUtilities.invokeLater(() -> messagesService.showMessage("DAngooo"));
        TimeUnit.SECONDS.sleep(60);
    }

}