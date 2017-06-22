package app;

import app.caseyellow.client.domain.services.impl.TestGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.net.UnknownHostException;

import static app.caseyellow.client.common.Messages.churchillSpeech;
import static app.caseyellow.client.infrastructre.AppBootHelper.bootAppWithArgs;
import static app.caseyellow.client.infrastructre.AppBootHelper.initForkJoinCommonPool;

/**
 * Created by dango on 6/2/17.
 */
@SpringBootApplication
public class App {

    // Logger
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

