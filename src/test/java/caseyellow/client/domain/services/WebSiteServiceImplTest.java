package caseyellow.client.domain.services;

import caseyellow.client.domain.website.model.OoklaSpeedTestWebSite;
import caseyellow.client.domain.website.service.WebSiteService;
import caseyellow.client.domain.website.service.WebSiteServiceImpl;
import caseyellow.client.infrastructre.BrowserServiceImpl;
import org.junit.Test;

/**
 * Created by Dan on 6/30/2017.
 */
public class WebSiteServiceImplTest {

    @Test
    public void produceSpeedTestWebSiteDownloadInfo() throws Exception {
        WebSiteServiceImpl webSiteService = new WebSiteServiceImpl();
        webSiteService.setBrowserService(new BrowserServiceImpl());
        webSiteService.produceSpeedTestWebSiteDownloadInfo(new OoklaSpeedTestWebSite());
    }

}