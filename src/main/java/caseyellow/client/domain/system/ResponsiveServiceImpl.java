package caseyellow.client.domain.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static caseyellow.client.common.Utils.moveMouseTo;

@Component
public class ResponsiveServiceImpl implements ResponsiveService {

    @Value("${sleep_interval_in_seconds}")
    private int sleepIntervalInSeconds;
    private ScheduledExecutorService keepAliveService;
    private ScheduledFuture<?> keepAliveTask;

    public ResponsiveServiceImpl() {
        keepAliveService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void keepAlive() {
        keepAliveTask = keepAliveService.scheduleAtFixedRate(() -> moveMouse(), 0L, sleepIntervalInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void close() throws IOException {
        keepAliveTask.cancel(true);
    }

    private void moveMouse() {
        Point currentMouseCursorLocation = MouseInfo.getPointerInfo().getLocation();
        int x = (int)currentMouseCursorLocation.getX();
        int y = (int)currentMouseCursorLocation.getY();

        int newXLocation = x > 0 ? x-1 : x+1;
        int newYLocation = y > 0 ? y-1 : y+1;

        moveMouseTo(newXLocation, newYLocation);
    }
}
