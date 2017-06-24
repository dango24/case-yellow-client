package caseyellow.client.config;

import caseyellow.client.presistance.DataAccessServiceImpl;
import caseyellow.client.domain.services.interfaces.DataAccessService;
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
    public DataAccessService dataAccessService() {
        return new DataAccessServiceImpl();
    }

    @Bean
    @Profile("!integration")
    public DataAccessService dataAccessServiceTest() {
        return new DataAccessServiceImpl();
    }
}
