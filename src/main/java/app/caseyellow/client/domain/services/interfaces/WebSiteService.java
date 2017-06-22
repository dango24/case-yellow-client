package app.caseyellow.client.domain.services.interfaces;

import app.caseyellow.client.domain.model.test_entites.SpeedTestWebSiteDownloadInfo;
import app.caseyellow.client.domain.model.web_site_entites.SpeedTestWebSite;

/**
 * Created by dango on 6/3/17.
 */
public interface WebSiteService {
    SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite);
}
