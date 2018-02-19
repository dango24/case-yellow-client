package caseyellow.client.domain.test.model;


/**
 * Created by Dan on 12/10/2016.
 */

public class SystemInfo {

    private String operatingSystem;
    private String browser;
    private String publicIP;
    private String connection; // LAN / Wifi connection

    public SystemInfo() {
    }

    public SystemInfo(String operatingSystem, String browser, String publicIP, String connection) {
        this.operatingSystem = operatingSystem;
        this.browser = browser;
        this.publicIP = publicIP;
        this.connection = connection;
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
