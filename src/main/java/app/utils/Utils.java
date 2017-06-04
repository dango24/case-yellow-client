package app.utils;

import app.exceptions.ConnectionTypeException;
import org.apache.log4j.Logger;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by dango on 6/2/17.
 */
public class Utils {

    // Logger
    private static Logger log;

    // Constants Variables
    public final static SimpleDateFormat formatter;

    // Init
    static {
        formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        log = Logger.getLogger(Utils.class);
    }

    // Utilities functions

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    public static String getConnection() {
        NetworkInterface networkInterface;

        try {
            networkInterface = getInternetConnection();
            return isEthernetConnection(networkInterface) ? "LAN" : "Wifi";

        } catch (Exception e) {
            log.error("Failed to find client connection type" + e.getMessage(), e);
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
