import org.apache.log4j.Logger;

import static utils.AppBootUtils.bootAppWithArgs;
import static utils.AppBootUtils.initForkJoinCommonPool;
import static utils.Messages.churchillSpeech;

/**
 * Created by dango on 6/2/17.
 */
public class App {

    // Logger
    final static Logger logger = Logger.getLogger(App.class);

    // Functions

    public static void main(String[] args) {

        bootAppWithArgs(args);
        initForkJoinCommonPool();

        logger.info(churchillSpeech());
    }

}
