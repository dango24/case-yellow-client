package caseyellow.client;

import caseyellow.client.domain.services.TestGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;

import java.net.UnknownHostException;

import static caseyellow.client.common.Messages.churchillSpeech;
import static caseyellow.client.infrastructre.AppBootHelper.bootAppWithArgs;
import static caseyellow.client.infrastructre.AppBootHelper.initForkJoinCommonPool;

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

        bootAppWithArgs(args);
        initForkJoinCommonPool();
        startTestGenerator(args);
    }

    private static void startTestGenerator(String[] args) {
        ApplicationContext ctx = SpringApplication.run(App.class, args);
        TestGenerator testGenerator = (TestGenerator)ctx.getBean("testGenerator");
        testGenerator.produceTests();
    }
}