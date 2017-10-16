package caseyellow.client.domain.website.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpeedTestWebSiteTest {


    @Test
    public void getKey() throws Exception {
        String snapshot = "dango_was_here_and_oren_efes.png";
        SpeedTestWebSite speedTestWebSite = new SpeedTestWebSite.SpeedTestWebSiteDownloadInfoBuilder("hot")
                                                                .setSucceed()
                                                                .setWebSiteDownloadInfoSnapshot(snapshot)
                                                                .setStartDownloadingTimeSnapshot(54543543)
                                                                .build();

        assertEquals(snapshot.hashCode(), speedTestWebSite.getKey());
    }

}