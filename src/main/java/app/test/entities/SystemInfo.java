package app.test.entities;


/**
 * Created by Dan on 12/10/2016.
 */

 // TODO dango add system info to test
public class SystemInfo {

    private String operatingSystem;
    private String browser;
    private String publicIP;
    private String connection; // LAN / Wifi connection

    public SystemInfo(String connection) {
//        this.connection = connection;
//        operatingSystem = DriverBase.getOperatingSystem();
//        browser = DriverBase.getSelectedDriverType().name();
//        publicIP = DriverBase.getPublicIPAddress();
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getBrowser() {
        return browser;
    }

    public String getPublicIP() {
        return publicIP;
    }

    public String getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "SystemInfo{" +
                "operatingSystem='" + operatingSystem + '\'' +
                ", browser='" + browser + '\'' +
                ", publicIP='" + publicIP + '\'' +
                ", connection='" + connection + '\'' +
                '}';
    }
}
