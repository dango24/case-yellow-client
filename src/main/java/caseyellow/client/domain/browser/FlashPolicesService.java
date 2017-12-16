package caseyellow.client.domain.browser;

import org.openqa.selenium.WebDriver;

public interface FlashPolicesService {
    void addPolicy(WebDriver webDriver, String url);
}
