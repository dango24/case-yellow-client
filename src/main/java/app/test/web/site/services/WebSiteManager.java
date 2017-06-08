package app.test.web.site.services;

import org.springframework.stereotype.Service;
import app.test.entities.SpeedTestWebSiteDownloadInfo;
import app.test.web.site.entities.SpeedTestWebSite;
import app.test.web.site.services.WebSiteService;

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
