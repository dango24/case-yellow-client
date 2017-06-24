package caseyellow.client.domain.model;


/**
 * Created by Dan on 12/10/2016.
 */

 // TODO dango add system info to urls
public class SystemInfo {

    private String operatingSystem;
    private String browser;
    private String publicIP;
    private String connection; // LAN / Wifi connection

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
