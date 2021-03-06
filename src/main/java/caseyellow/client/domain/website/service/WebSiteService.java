package caseyellow.client.domain.website.service;

import caseyellow.client.domain.website.model.SpeedTestMetaData;
import caseyellow.client.domain.website.model.SpeedTestWebSite;
import caseyellow.client.exceptions.ConnectionException;
import caseyellow.client.exceptions.UserInterruptException;

import java.io.Closeable;

/**
 * Created by dango on 6/3/17.
 */
public interface WebSiteService extends Closeable {
    SpeedTestWebSite produceSpeedTestWebSite(SpeedTestMetaData speedTestWebsite) throws UserInterruptException, ConnectionException;
}
