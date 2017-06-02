package utils;

import exceptions.ConnectionTypeException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Created by dango on 6/2/17.
 */
public class Utils {

    // Logger
    private static Logger log;

    // Constants Variables
    public final static SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        log = Logger.getLogger(Utils.class);
    }

    // Utilities functions

    public static void bootAppWithArgs(String[] bootArgs) {
        Map<String, String> argsMap = buildArgsKeyValueParis(bootArgs);
        updateLog4jConfiguration(argsMap.get("logFilePath"));
    }

    private static Map<String, String> buildArgsKeyValueParis(String[] bootArgs) {

        return Stream.of(bootArgs)
                     .filter(arg -> arg.startsWith("-D"))
                     .map(arg -> arg.replace("-D", ""))
                     .filter(arg -> !arg.isEmpty())
                     .map(arg -> arg.split("="))
                     .filter(argKeyValuePair -> argKeyValuePair.length == 2)
                     .collect(toMap(argKeyValuePair -> argKeyValuePair[0],
                                    argKeyValuePair -> argKeyValuePair[1]));
    }

    private static void updateLog4jConfiguration(String logFile) {
        Properties props = new Properties();

        if (logFile == null || logFile.isEmpty()) {
            logFile = createDefaultLoggingFile();
        }

        try (InputStream configStream = Utils.class.getResourceAsStream( "/log4j.properties")){
            props.load(configStream);

        } catch (IOException e) {
            System.out.println("Error not load configuration file ");
        }

        props.setProperty("log4j.appender.file.File", logFile);
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(props);
    }

    private static String createDefaultLoggingFile() {
        File logDir = new File(System.getProperty("user.dir"), "logs");

        if (!logDir.exists()) {
            logDir.mkdir();
        }

        return new File(logDir, "logging.log").toString();
    }

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    public static String getConnection() {
        NetworkInterface networkInterface;

        try {
            networkInterface = getInternetConnection();
            return isEthernetConnection(networkInterface) ? "LAN" : "Wifi";

        } catch (Exception e) {
            log.error("Failed to find client connection type" + e.getMessage());
            return "Unknown";
        }
    }

    private static NetworkInterface getInternetConnection() throws SocketException {

        return Collections.list(NetworkInterface.getNetworkInterfaces())
                          .stream()
                          .filter(Utils::isUp)
                          .filter(Utils::isHardwareAddress)
                          .findFirst()
                          .orElseThrow(() -> new ConnectionTypeException("Unknown connection"));
    }

    private static boolean isUp(NetworkInterface networkInterface) {
        try {
            return networkInterface.isUp();
        } catch (SocketException e) {
            throw new ConnectionTypeException(e.getMessage());
        }
    }

    private static boolean isHardwareAddress(NetworkInterface networkInterface) {
        try {
            return networkInterface.getHardwareAddress() != null;
        } catch (SocketException e) {
            throw new ConnectionTypeException(e.getMessage());
        }
    }

    private static boolean isEthernetConnection(NetworkInterface networkInterface) {

        return Stream.of(networkInterface)
                     .filter(Objects::nonNull)
                     .map(NetworkInterface::getName)
                     .filter(Objects::nonNull)
                     .map(connectionName -> connectionName.contains("eth"))
                     .findFirst()
                     .orElse(false);
    }

    public static double round(double value, int places) {

        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);

        return (double) tmp / factor;
    }

    public static String format(Date date) {
        return formatter.format(date);
    }

}
