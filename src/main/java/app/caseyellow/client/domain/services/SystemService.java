package app.caseyellow.client.domain.services;

import app.caseyellow.client.domain.model.SystemInfo;

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
}
