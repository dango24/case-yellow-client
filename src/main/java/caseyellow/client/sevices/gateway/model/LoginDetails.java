package caseyellow.client.sevices.gateway.model;

public class LoginDetails {

    private boolean succeed;
    private boolean registration;

    public LoginDetails() {
    }

    public LoginDetails(boolean succeed) {
        this.succeed = succeed;
    }

    public LoginDetails(boolean succeed, boolean registration) {
        this.succeed = succeed;
        this.registration = registration;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }
}
