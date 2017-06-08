package app.utils;

import app.exceptions.ConnectionTypeException;
import app.test.entities.SystemInfo;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by dango on 6/3/17.
 */

// TODO dango add system info to test
public class SystemUtils {

    // Logger
    private static Logger log = Logger.getLogger(SystemUtils.class);

    public static SystemInfo getSystemInfo() {
        return null;
    }

    public static String getPublicIPAddress() {
        try {

            URL amazonawsCheckIP = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(amazonawsCheckIP.openStream()));
            String ip = in.readLine(); // Get the IP as a String

            return ip;

        } catch (IOException e) {
            log.error("Failed to retrieve public IP address " + e.getMessage());
            return "Unknown public IP";
        }
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
                .filter(SystemUtils::isUp)
                .filter(SystemUtils::isHardwareAddress)
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
}
