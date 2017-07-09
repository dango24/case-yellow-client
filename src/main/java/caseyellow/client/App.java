package caseyellow.client;

import caseyellow.client.presentation.MainForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.net.UnknownHostException;

import static caseyellow.client.common.Messages.churchillSpeech;
import static caseyellow.client.infrastructre.AppBootInitializer.bootApp;
import static caseyellow.client.infrastructre.AppBootInitializer.initForkJoinCommonPool;

/**
 * Created by dango on 6/2/17.
 */
@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class,
                                  WebMvcAutoConfiguration.class})
public class App {

    public final static Logger logger = Logger.getLogger(App.class);

    // Functions

    public static void main(String[] args) throws UnknownHostException {
        logger.info(churchillSpeech());
        initForkJoinCommonPool();
        bootApp(args);
        startApp(args);
    }

    private static void startApp(String[] args) {
        try {
            ApplicationContext ctx = SpringApplication.run(App.class, args);
            MainForm mainForm = (MainForm) ctx.getBean("mainForm");
            mainForm.view();
        } catch (Exception e) {
            logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, "The best app ever failed to initialized, " + e.getMessage());
        }
    }
}