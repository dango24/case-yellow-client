package caseyellow.client.domain.test.model;

public class ConnectionDetails {

    private int speed;
    private String isp;
    private String infrastructure;

    public ConnectionDetails() {
    }

    public ConnectionDetails(String infrastructure, String isp, int speed) {
        this.speed = speed;
        this.isp = isp;
        this.infrastructure = infrastructure;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getInfrastructure() {
        return infrastructure;
    }

    public void setInfrastructure(String infrastructure) {
        this.infrastructure = infrastructure;
    }
}
