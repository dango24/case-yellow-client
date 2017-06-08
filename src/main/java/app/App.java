package app;

import app.engine.TestGenerator;
import app.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static app.utils.AppBootUtils.bootAppWithArgs;
import static app.utils.AppBootUtils.initForkJoinCommonPool;
import static app.utils.Messages.churchillSpeech;

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
        testGenerator.doooo();
    }

}

