package speed.test.web.site.services;

import speed.test.entities.SpeedTestWebSiteDownloadInfo;
import speed.test.web.site.entities.SpeedTestWebSite;

/**
 * Created by dango on 6/3/17.
 */
public interface WebSiteService {
    SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite);
}
