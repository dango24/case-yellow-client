package app.test.web.site.services;

import app.test.entities.SpeedTestWebSiteDownloadInfo;
import app.test.web.site.entities.SpeedTestWebSite;

/**
 * Created by dango on 6/3/17.
 */
public interface WebSiteService {
    SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite);
}
