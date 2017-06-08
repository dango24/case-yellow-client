package app.config;

import app.access.DataAccessManager;
import app.access.DataAccessService;
import app.mock.access.DataAccessManagerMock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by dango on 6/6/17.
 */
@Configuration
public class AccessConfiguration {

    // Fields
    @Value("${speed-test-web-site-package}")
    private String packageNamePrefix;

    @Bean
    @Profile("integration")
    public DataAccessService dataAccessServiceDevelopment() {
        return new DataAccessManager();
    }

    @Bean
    @Profile("!integration")
    public DataAccessService dataAccessServiceTest() {
        return new DataAccessManagerMock(packageNamePrefix + "HotSpeedTestWebSite");
    }
}
