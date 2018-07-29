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
import caseyellow.client.exceptions.LoginException;
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
            if (args.length == 0) {
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

            if (isCmdLineMode(args)) {
                executeCmdLineMode(args[0], ctx, testGenerator, gatewayService);
            } else {
                executeGUIMode(testGenerator, messagesService, gatewayService);
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, "The best app ever failed to initialized, " + e.getMessage());
            mainForm.terminate();
        }
    }

    private static void executeGUIMode(TestGeneratorImpl testGenerator, MessageServiceImp messagesService, GatewayService gatewayService) {
        testGenerator.setMainFrame(mainForm);
        messagesService.setPresentationMessagesService(mainForm);
        mainForm.setStartProducingTestsCommand(testGenerator);
        mainForm.setStopProducingTestsCommand(testGenerator);
        mainForm.setGatewayService(gatewayService);
        mainForm.enableApp();
    }

    private static void executeCmdLineMode(String configPath, ApplicationContext ctx, TestGeneratorImpl testGenerator, GatewayService gatewayService) throws IOException, LoginException {

        System.setProperty("java.awt.headless", "false");
        injectCmdLineMessageService(ctx);
        testGenerator.setMainFrame(new MainFrameImpl());

        LoginDetails loginDetails = gatewayService.login(createAccountCredentials(configPath));

        if (loginDetails.isSucceed()) {

            if (loginDetails.isRegistration()) {
                gatewayService.saveConnectionDetails(createConnectionDetails(configPath));
            }

            testGenerator.startProducingTests() ;

        } else {
            System.err.println("Failed to login, Aborting...");
        }
    }

    private static void injectCmdLineMessageService(ApplicationContext ctx) {
        TestServiceImpl testService = (TestServiceImpl)ctx.getBean("testServiceImpl");
        WebSiteServiceImpl webSiteService = (WebSiteServiceImpl)ctx.getBean("webSiteServiceImpl");
        DownloadFileServiceImpl downloadFileService = (DownloadFileServiceImpl) ctx.getBean("downloadFileServiceImpl");
        SystemServiceImpl systemService = (SystemServiceImpl)ctx.getBean("systemServiceImpl");

        CmdLineMessageImpl cmdLineMessage = new CmdLineMessageImpl();
        testService.setMessagesService(cmdLineMessage);
        webSiteService.setMessagesService(cmdLineMessage);
        downloadFileService.setMessagesService(cmdLineMessage);
        systemService.setMessagesService(cmdLineMessage);
    }

    private static AccountCredentials createAccountCredentials(String configPath) throws IOException {
        String rawConfiguration = FileUtils.readFile(configPath);
        String[] rawFields = rawConfiguration.split("\n");
        String user = rawFields[0].trim();
        String password = rawFields[1].trim();

        return new AccountCredentials(user, password);
    }

    private static ConnectionDetails createConnectionDetails(String configPath) throws IOException {
        String rawConfiguration = FileUtils.readFile(configPath);
        String[] rawFields = rawConfiguration.split("\n");
        String infra = rawFields[2].trim();
        int speed = Integer.valueOf(rawFields[3].trim());

        return new ConnectionDetails(infra, speed);
    }

    private static boolean isCmdLineMode(String[] args) {
        return args.length > 0;
    }
}