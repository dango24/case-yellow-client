package app.caseyellow.client.config;

import app.caseyellow.client.infrastructre.UrlMapper;
import app.caseyellow.client.presistance.DataAccessManager;
import app.caseyellow.client.domain.services.interfaces.DataAccessService;
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
        return null; //return new DataAccessManagerMock(packageNamePrefix + "HotSpeedTestWebSite");
    }

    @Bean
    public UrlMapper.UrlName urls() {
        return new UrlMapper.UrlName();
    }
}
