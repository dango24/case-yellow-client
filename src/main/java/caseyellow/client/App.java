package caseyellow.client;

import caseyellow.client.domain.test.service.TestGeneratorImpl;
import caseyellow.client.domain.message.MessageServiceImp;
import caseyellow.client.presentation.MainFrameImpl;
import caseyellow.client.sevices.gateway.services.GatewayService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.JOptionPane;
import java.io.IOException;


/**
 * Created by dango on 6/2/17.
 */
@EnableScheduling
@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class, WebMvcAutoConfiguration.class})
public class App {

    private final static Logger logger = Logger.getLogger(App.class);

    private static MainFrameImpl mainForm;

    public static void main(String[] args) throws Exception {
        try {
            initView();
            initApplicationContext(args);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void initView() throws IOException {
        mainForm = new MainFrameImpl();
        mainForm.view();
    }

    private static void initApplicationContext(String[] args) {

        try {
            ApplicationContext ctx = SpringApplication.run(App.class, args);

            TestGeneratorImpl testService = (TestGeneratorImpl) ctx.getBean("testGenerator");
            MessageServiceImp messagesService = (MessageServiceImp) ctx.getBean("messageServiceImp");
            GatewayService gatewayService = (GatewayService) ctx.getBean("gatewayService");

            testService.setMainFrame(mainForm);
            messagesService.setPresentationMessagesService(mainForm);
            mainForm.setStartProducingTestsCommand(testService);
            mainForm.setStopProducingTestsCommand(testService);
            mainForm.setGatewayService(gatewayService);
            mainForm.enableApp();

        } catch (Exception e) {
            logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, "The best app ever failed to initialized, " + e.getMessage());
            mainForm.terminate();
        }
    }
}