package caseyellow.client.domain.services.impl;

import caseyellow.client.domain.model.test.SpeedTestWebSiteDownloadInfo;
import caseyellow.client.domain.model.website.SpeedTestWebSite;
import caseyellow.client.domain.services.interfaces.WebSiteService;
import org.springframework.stereotype.Service;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class WebSiteServiceImpl implements WebSiteService {

    @Override
    public SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite) {
        System.out.println("SpeedTestWebSiteDownloadInfo " + this.getClass().getName());
        return null;
    }
}
