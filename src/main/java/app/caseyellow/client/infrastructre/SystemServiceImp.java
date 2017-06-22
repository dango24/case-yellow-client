package app.caseyellow.client.infrastructre;

import app.caseyellow.client.domain.model.SystemInfo;
import app.caseyellow.client.domain.services.interfaces.SystemService;
import app.caseyellow.client.exceptions.ConnectionTypeException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static app.caseyellow.client.domain.services.interfaces.SystemService.LAN_CONNECTION;
import static app.caseyellow.client.domain.services.interfaces.SystemService.UNKNOWN_CONNECTION;
import static app.caseyellow.client.domain.services.interfaces.SystemService.WIFI_CONNECTION;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class SystemServiceImp implements SystemService {

    // Constants
    private static final String ETHERNET_IDENTIFIER = "eth";

    // Logger
    private Logger log = Logger.getLogger(SystemServiceImp.class);

    // Methods

    @Override
    public SystemInfo getSystemInfo() {
        String connection = getConnection();
        String ipAddress = getPublicIPAddress();
        String os = getOperationSystem();
        String browser = getBrowser();

        return new SystemInfo(os, browser, ipAddress, connection);
    }

    private String getOperationSystem() {
        return System.getProperty("os.name").toUpperCase();
    }

    private String getPublicIPAddress() {

        try {
            URL amazonAwsCheckIP = new URL("http://checkip.amazonaws.com");
            return IOUtils.toString(amazonAwsCheckIP, "UTF-8");

        } catch (IOException e) {
            log.error("Failed to retrieve public IP address " + e.getMessage());
            return UNKNOWN_CONNECTION;
        }
    }

    private String getConnection() {
        NetworkInterface networkInterface;

        try {
            networkInterface = getInternetConnection();
            return isEthernetConnection(networkInterface) ? LAN_CONNECTION : WIFI_CONNECTION;

        } catch (Exception e) {
            log.error("Failed to find client connection type" + e.getMessage(), e);
            return "Unknown";
        }
    }

    private NetworkInterface getInternetConnection() throws SocketException {

        return Collections.list(NetworkInterface.getNetworkInterfaces())
                          .stream()
                          .filter(this::isUp)
                          .filter(this::isHardwareAddress)
                          .findFirst()
                          .orElseThrow(() -> new ConnectionTypeException("Unknown connection"));
    }

    private boolean isUp(NetworkInterface networkInterface) {
        try {
            return networkInterface.isUp();
        } catch (SocketException e) {
            throw new ConnectionTypeException(e.getMessage());
        }
    }

    private boolean isHardwareAddress(NetworkInterface networkInterface) {
        try {
            return networkInterface.getHardwareAddress() != null;
        } catch (SocketException e) {
            throw new ConnectionTypeException(e.getMessage());
        }
    }

    private boolean isEthernetConnection(NetworkInterface networkInterface) {

        return Stream.of(networkInterface)
                     .filter(Objects::nonNull)
                     .map(NetworkInterface::getName)
                     .filter(Objects::nonNull)
                     .map(connectionName -> connectionName.contains(ETHERNET_IDENTIFIER))
                     .findFirst()
                     .orElse(false);
    }

    private String getBrowser() {
        return ""; // todo dango create getBrowser method
    }
}
