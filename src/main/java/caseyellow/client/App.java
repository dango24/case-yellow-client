package caseyellow.client;

import caseyellow.client.common.FileUtils;
import caseyellow.client.domain.file.service.DownloadFileServiceImpl;
import caseyellow.client.domain.message.CmdLineMessageImpl;
import caseyellow.client.domain.system.SystemServiceImpl;
import caseyellow.client.domain.test.model.ConnectionDetails;
import caseyellow.client.domain.test.service.TestGeneratorImpl;
import caseyellow.client.domain.message.MessageServiceImp;
import caseyellow.client.domain.test.service.TestServiceImpl;
import caseyellow.client.domain.website.service.WebSiteServiceImpl;
import caseyellow.client.presentation.MainFrameImpl;
import caseyellow.client.sevices.gateway.model.AccountCredentials;
import caseyellow.client.sevices.gateway.model.LoginDetails;
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
            if(args.length < 1)
            {
                initView();
            }

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
            TestGeneratorImpl testGenerator = (TestGeneratorImpl) ctx.getBean("testGenerator");
            MessageServiceImp messagesService = (MessageServiceImp) ctx.getBean("messageServiceImp");
            GatewayService gatewayService = (GatewayService) ctx.getBean("gatewayService");

            if(args.length > 0) {
                // configuration file mode

                System.setProperty("java.awt.headless", "false");

                TestServiceImpl testService = (TestServiceImpl)ctx.getBean("testServiceImpl");
                WebSiteServiceImpl webSiteService = (WebSiteServiceImpl)ctx.getBean("webSiteServiceImpl");
                DownloadFileServiceImpl downloadFileService = (DownloadFileServiceImpl) ctx.getBean("downloadFileServiceImpl");
                SystemServiceImpl systemService = (SystemServiceImpl)ctx.getBean("systemServiceImpl");

                CmdLineMessageImpl cmdLineMessage = new CmdLineMessageImpl();
                testService.setMessagesService(cmdLineMessage);
                webSiteService.setMessagesService(cmdLineMessage);
                downloadFileService.setMessagesService(cmdLineMessage);
                systemService.setMessagesService(cmdLineMessage);
                testGenerator.setMainFrame(new MainFrameImpl());

                String rawConfiguration = FileUtils.readFile(args[0]);
                String[] rawFields = rawConfiguration.split("\n");
                String user = rawFields[0].trim();
                String password = rawFields[1].trim();



                LoginDetails loginDetails = gatewayService.login(new AccountCredentials(user, password));
                if (loginDetails.isSucceed()) {
                    if (loginDetails.isRegistration()) {
                        String infra = rawFields[2].trim();
                        int speed = Integer.valueOf(rawFields[3].trim());
                        gatewayService.saveConnectionDetails(new ConnectionDetails(infra, speed));
                    }

                    testGenerator.startProducingTests() ;
                    return;
                }
                else System.out.println("Failed to login, Aborting...");
                return;
            }
            
            testGenerator.setMainFrame(mainForm);
            messagesService.setPresentationMessagesService(mainForm);
            mainForm.setStartProducingTestsCommand(testGenerator);
            mainForm.setStopProducingTestsCommand(testGenerator);
            mainForm.setGatewayService(gatewayService);
            mainForm.enableApp();

        } catch (Exception e) {
            logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, "The best app ever failed to initialized, " + e.getMessage());
            mainForm.terminate();
        }
    }
}