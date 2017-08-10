package caseyellow.client.exceptions;

import org.openqa.selenium.WebDriverException;

public class UserInterruptException extends WebDriverException {

    public UserInterruptException(String message) {
        super(message);
    }

    public UserInterruptException(String message, Throwable cause) {
        super(message, cause);
    }
}
