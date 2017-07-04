package caseyellow.client.domain.website.service;

import caseyellow.client.domain.website.model.SpeedTestWebSiteDownloadInfo;
import caseyellow.client.domain.website.model.SpeedTestWebSite;

/**
 * Created by dango on 6/3/17.
 */
public interface WebSiteService {
    SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite);
}
