import org.apache.log4j.Logger;
import utils.Utils;

import static utils.Messages.churchillSpeech;

/**
 * Created by dango on 6/2/17.
 */
public class App {

    // Logger
    final static Logger logger = Logger.getLogger(App.class);


    public static void main(String[] args) {
        Utils.bootAppWithArgs(args);
        logger.info(churchillSpeech());
    }

}
