package caseyellow.client.domain.services.interfaces;

import caseyellow.client.domain.model.test.SpeedTestWebSiteDownloadInfo;
import caseyellow.client.domain.model.website.SpeedTestWebSite;

/**
 * Created by dango on 6/3/17.
 */
public interface WebSiteService {
    SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite);
}
