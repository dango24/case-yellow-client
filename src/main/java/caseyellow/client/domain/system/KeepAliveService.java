package caseyellow.client.domain.system;

import caseyellow.client.domain.test.service.TestGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;

@Service
public class KeepAliveService {

    private Logger logger = Logger.getLogger(KeepAliveService.class);

    private final static int INITIAL_DELAY = 60_000;
    private final static int KEEP_ALIVE_INTERVAL = 1_000;

    private TestGenerator testGenerator;

    @Autowired
    public KeepAliveService(TestGenerator testGenerator) {
        this.testGenerator = testGenerator;
    }

    @Scheduled(initialDelay = INITIAL_DELAY, fixedDelay = KEEP_ALIVE_INTERVAL)
    private void keepAlive() throws InterruptedException {
        boolean isAlive = isAlive();

        if (!isAlive) {
            logger.warn("Connection lost");
//            testGenerator.handleLostConnection();
        }
    }

    private boolean isAlive() {
        try {
            Process keepAliveProcess = getRuntime().exec("ping -n 1 www.google.com");
            int returnVal = keepAliveProcess.waitFor();

            return returnVal == 0;

        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
