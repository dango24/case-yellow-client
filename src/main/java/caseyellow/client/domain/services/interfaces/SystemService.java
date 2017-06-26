package caseyellow.client.domain.services.interfaces;

import caseyellow.client.domain.model.SystemInfo;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dan on 6/20/2017.
 */
public interface SystemService {

    // Constants
    String LAN_CONNECTION = "LAN";
    String WIFI_CONNECTION = "Wifi";
    String UNKNOWN_CONNECTION = "Unknown";

    // Methods
    SystemInfo getSystemInfo();
    void deleteDirectory(File directory) throws IOException;
}
