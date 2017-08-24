package caseyellow.client.infrastructre;

import caseyellow.client.App;
import caseyellow.client.common.Mapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Created by dango on 6/3/17.
 */
public class AppBootInitializer {


    public static void initAppPreRequirements(String[] bootArgs) {
        Map<String, String> argsMap = buildArgsKeyValueParis(bootArgs);
    }

    private static Map<String, String> buildArgsKeyValueParis(String[] bootArgs) {

        return Stream.of(bootArgs)
                     .filter(arg -> arg.startsWith("-D")) // argument identifier
                     .map(arg -> arg.substring(2)) // remove '-D' from the argument
                     .filter(arg -> !arg.isEmpty())
                     .map(arg -> arg.split("=")) // separate to key value
                     .filter(argKeyValuePair -> argKeyValuePair.length == 2) // validate schema
                     .collect(toMap(argKeyValuePair -> argKeyValuePair[0],
                                    argKeyValuePair -> argKeyValuePair[1]));
    }

    // ForkJoinCommonPool is lazy initialized, there for at app boot make a dummy
    // request for ForkJoinCommonPool initialization
    public static void initForkJoinCommonPool() {
        CompletableFuture.supplyAsync(() -> "Init ForkJoinCommonPool at start-up")
                         .thenAccept(output -> App.logger.info(output));
    }
}
