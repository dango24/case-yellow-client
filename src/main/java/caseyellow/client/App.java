package caseyellow.client;

import caseyellow.client.domain.test.service.TestGenerator;
import caseyellow.client.infrastructre.MessageServiceImp;
import caseyellow.client.presentation.MainForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;

import javax.swing.JOptionPane;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;


/**
 * Created by dango on 6/2/17.
 */
@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class,
                                  WebMvcAutoConfiguration.class})
public class App {

    private final static Logger logger = Logger.getLogger(App.class);

    private static MainForm mainForm;

    public static void main(String[] args) throws UnknownHostException {
        initView();
        initForkJoinCommonPool();
        initApplicationContext(args);
    }

    private static void initView() {
        mainForm = new MainForm();
        mainForm.view();
    }

    // ForkJoinCommonPool is lazy initialized, there for at app boot make a dummy
    // request for ForkJoinCommonPool initialization
    private static void initForkJoinCommonPool() {
        CompletableFuture.supplyAsync(() -> "Init ForkJoinCommonPool at start-up")
                         .thenAccept(output -> App.logger.info(output));
    }

    private static void initApplicationContext(String[] args) {

        try {
            ApplicationContext ctx = SpringApplication.run(App.class, args);

            TestGenerator testService = (TestGenerator)ctx.getBean("testGenerator");
            MessageServiceImp messagesService = (MessageServiceImp)ctx.getBean("messageServiceImp");

            messagesService.setPresentationMessagesService(mainForm);
            mainForm.setStartProducingTestsCommand(testService);
            mainForm.setStopProducingTestsCommand(testService);
            mainForm.enableApp();

        } catch (Exception e) {
            logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, "The best app ever failed to initialized, " + e.getMessage());
            mainForm.terminate();
        }
    }
}