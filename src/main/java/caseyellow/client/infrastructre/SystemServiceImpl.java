package caseyellow.client.infrastructre;

import caseyellow.client.domain.test.model.SystemInfo;
import caseyellow.client.domain.interfaces.BrowserService;
import caseyellow.client.domain.interfaces.SystemService;
import caseyellow.client.exceptions.ConnectionTypeException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class SystemServiceImpl implements SystemService {

    // Constants
    private static final String ETHERNET_IDENTIFIER = "eth";

    // Logger
    private Logger log = Logger.getLogger(SystemServiceImpl.class);

    private BrowserService browserService;

    public SystemServiceImpl() {
        this(null);
    }

    @Autowired
    public SystemServiceImpl(BrowserService browserService) {
        this.browserService = browserService;
    }

    @Override
    public SystemInfo getSystemInfo() {
        String connection = getConnection();
        String ipAddress = getPublicIPAddress();
        String os = getOperationSystem();
        String browser = getBrowser();

        return new SystemInfo(os, browser, ipAddress, connection);
    }

    @Override
    public void deleteDirectory(File directory) throws IOException {
        FileUtils.deleteDirectory(directory);
    }

    @Override
    public void copyURLToFile(URL source, File destination) throws IOException {
        FileUtils.copyURLToFile(source, destination);
    }

    private String getOperationSystem() {
        return System.getProperty("os.name").toUpperCase();
    }

    private String getPublicIPAddress() {

        try {
            URL amazonAwsCheckIP = new URL("http://checkip.amazonaws.com");
            return IOUtils.toString(amazonAwsCheckIP, "UTF-8").replace("\n", "");

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
            return UNKNOWN_CONNECTION;
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
        return browserService.getBrowserName();
    }

}