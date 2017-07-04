package caseyellow.client.domain.services;

import caseyellow.client.common.Mapper;
import caseyellow.client.domain.website.model.ATNTSpeedTestWebSite;
import caseyellow.client.domain.website.model.HotSpeedTestWebSite;
import caseyellow.client.domain.website.model.OoklaSpeedTestWebSite;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.domain.website.service.SpeedTestWebSiteFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dango on 6/27/17.
 */
public class SpeedTestWebSiteFactoryTest {

    private static SpeedTestWebSiteFactory speedTestWebSiteFactory;

    @BeforeClass
    public static void setUp() throws Exception {
        Mapper mapper = mock(Mapper.class);
        when(mapper.getWebSiteClassFromIdentifier("hot")).thenReturn("caseyellow.client.domain.website.model.HotSpeedTestWebSite");
        when(mapper.getWebSiteClassFromIdentifier("ookla")).thenReturn("caseyellow.client.domain.website.model.OoklaSpeedTestWebSite");
        when(mapper.getWebSiteClassFromIdentifier("atnt")).thenReturn("caseyellow.client.domain.website.model.ATNTSpeedTestWebSite");

        speedTestWebSiteFactory = new SpeedTestWebSiteFactory(mapper);
    }

    @Test
    public void createSpeedTestWebSiteFromIdentifierHot() throws Exception {
        SpeedTestWebSite hotSpeedTestWebSite = speedTestWebSiteFactory.createSpeedTestWebSiteFromIdentifier("hot");
        assertTrue(hotSpeedTestWebSite instanceof HotSpeedTestWebSite);
    }

    @Test
    public void createSpeedTestWebSiteFromIdentifierOokla() throws Exception {
        SpeedTestWebSite ooklaSpeedTestWebSite = speedTestWebSiteFactory.createSpeedTestWebSiteFromIdentifier("ookla");
        assertTrue(ooklaSpeedTestWebSite instanceof OoklaSpeedTestWebSite);
    }

    @Test
    public void createSpeedTestWebSiteFromIdentifierAtnt() throws Exception {
        SpeedTestWebSite atntSpeedTestWebSite = speedTestWebSiteFactory.createSpeedTestWebSiteFromIdentifier("atnt");
        assertTrue(atntSpeedTestWebSite instanceof ATNTSpeedTestWebSite);
    }

}