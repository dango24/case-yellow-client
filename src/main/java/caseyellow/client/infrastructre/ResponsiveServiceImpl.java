package caseyellow.client.infrastructre;

import caseyellow.client.common.Utils;
import caseyellow.client.domain.interfaces.ResponsiveService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ResponsiveServiceImpl implements ResponsiveService {

    @Value("${sleep_interval_in_seconds}")
    private int sleepIntervalInSeconds;
    private ScheduledExecutorService keepAliveService;

    public ResponsiveServiceImpl() {
        keepAliveService = Executors.newSingleThreadScheduledExecutor();
    }

    @PostConstruct
    public void init() {
        keepAliveService.scheduleAtFixedRate(() -> keepAlive(), 0L, sleepIntervalInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void keepAlive() {
        Point currentMouseCursorLocation = MouseInfo.getPointerInfo().getLocation();
        int x = (int)currentMouseCursorLocation.getX();
        int y = (int)currentMouseCursorLocation.getY();

        int newXLocation = x > 0 ? x-1 : x+1;
        int newYLocation = y > 0 ? y-1 : y+1;

        Utils.moveMouseTo(newXLocation, newYLocation);
    }

    @Override
    public void sleep() {

    }
}
