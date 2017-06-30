package caseyellow.client.domain.services;

import caseyellow.client.domain.model.website.OoklaSpeedTestWebSite;
import caseyellow.client.domain.services.interfaces.WebSiteService;
import caseyellow.client.infrastructre.BrowserServiceImpl;
import caseyellow.client.infrastructre.SystemServiceImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Dan on 6/30/2017.
 */
public class WebSiteServiceImplTest {

    @Test
    public void produceSpeedTestWebSiteDownloadInfo() throws Exception {
        WebSiteService webSiteService = new WebSiteServiceImpl(new BrowserServiceImpl());
        webSiteService.produceSpeedTestWebSiteDownloadInfo(new OoklaSpeedTestWebSite());
    }

}