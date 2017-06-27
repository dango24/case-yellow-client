package caseyellow.client.domain.services;

import caseyellow.client.common.Mapper;
import caseyellow.client.domain.model.website.HotSpeedTestWebSite;
import caseyellow.client.domain.model.website.SpeedTestWebSite;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by dango on 6/27/17.
 */
@Component
public class SpeedTestWebSiteFactory {

    private final Logger logger = Logger.getLogger(SpeedTestWebSiteFactory.class);
    private final Mapper mapper;

    @Autowired
    public SpeedTestWebSiteFactory(Mapper mapper) {
        this.mapper = mapper;
    }

    public SpeedTestWebSite createSpeedTestWebSiteFromIdentifier(String identifier) {

        try {
            Class<?> speedTestWebSiteClass = Class.forName(mapper.getWebSiteClassFromIdentifier(identifier));
            SpeedTestWebSite speedTestWebSite = (SpeedTestWebSite)speedTestWebSiteClass.newInstance();

            return speedTestWebSite;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
            return new HotSpeedTestWebSite(); // If failed send 'HOT' speed test web site as default
        }
    }
}
