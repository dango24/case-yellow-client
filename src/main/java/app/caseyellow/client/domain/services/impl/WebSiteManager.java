package app.caseyellow.client.domain.services.impl;

import app.caseyellow.client.domain.model.test_entites.SpeedTestWebSiteDownloadInfo;
import app.caseyellow.client.domain.model.web_site_entites.SpeedTestWebSite;
import app.caseyellow.client.domain.services.WebSiteService;
import org.springframework.stereotype.Service;

/**
 * Created by dango on 6/3/17.
 */
@Service
public class WebSiteManager implements WebSiteService {

    @Override
    public SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite) {
        System.out.println("SpeedTestWebSiteDownloadInfo " + this.getClass().getName());
        return null;
    }
}
