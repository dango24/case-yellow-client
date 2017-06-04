package utils;

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
public class AppBootUtils {

    // App boot utilities functions

    public static void bootAppWithArgs(String[] bootArgs) {
        Map<String, String> argsMap = buildArgsKeyValueParis(bootArgs);
        updateLog4jConfiguration(argsMap.get("logFilePath"));
    }

    private static Map<String, String> buildArgsKeyValueParis(String[] bootArgs) {

        return Stream.of(bootArgs)
                .filter(arg -> arg.startsWith("-D")) // argument identifier
                .map(arg -> arg.substring(2)) // remove '-D' from the argument
                .filter(arg -> !arg.isEmpty())
                .map(arg -> arg.split("=")) // separate to key value
                .filter(argKeyValuePair -> argKeyValuePair.length == 2)
                .collect(toMap(argKeyValuePair -> argKeyValuePair[0],
                               argKeyValuePair -> argKeyValuePair[1]));
    }

    private static void updateLog4jConfiguration(String logFile) {
        Properties props = new Properties();

        if (logFile == null || logFile.isEmpty()) {
            logFile = createDefaultLoggingFile();
        }

        try (InputStream configStream = Utils.class.getResourceAsStream( "/log4j.properties")) {
            props.load(configStream);

        } catch (IOException e) {
            System.out.println("Error: failed to load log4j configuration file");
        }

        props.setProperty("log4j.appender.file.File", logFile);
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }

    private static String createDefaultLoggingFile() {
        File logDir = new File(System.getProperty("user.home"), "case-yellow-logs");

        if (!logDir.exists()) {
            logDir.mkdir();
        }

        return new File(logDir, "logging.log").toString();
    }

    // ForkJoinCommonPool is lazy initialized, there for at app boot make a dummy
    // request for ForkJoinCommonPool initialization
    public static void initForkJoinCommonPool() {
        CompletableFuture.supplyAsync(() -> "Init ForkJoinCommonPool at start-up")
                         .thenAccept(System.out::println);
    }
}
