package speed.test.web.site.engine;

import speed.test.entities.SpeedTestWebSiteDownloadInfo;
import speed.test.web.site.entities.SpeedTestWebSite;
import speed.test.web.site.services.WebSiteService;

/**
 * Created by dango on 6/3/17.
 */
public class WebSiteManager implements WebSiteService {

    @Override
    public SpeedTestWebSiteDownloadInfo produceSpeedTestWebSiteDownloadInfo(SpeedTestWebSite speedTestWebsite) {
        System.out.println("SpeedTestWebSiteDownloadInfo " + this.getClass().getName());
        return null;
    }
}
