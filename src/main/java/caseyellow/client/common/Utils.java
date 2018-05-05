package caseyellow.client.common;

import caseyellow.client.exceptions.InternalFailureException;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Created by Dan on 6/20/2017.
 */
@Slf4j
public class Utils {

    private static ReentrantLock mouseEventLock;

    static {
        mouseEventLock = new ReentrantLock();
    }

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    public static void click(int x, int y) {
        IntStream.range(0, 3).forEach(attempt -> clickImage(x, y));
        log.info("Successfully clicked image");
    }

    private static void clickImage(int x, int y) {
        try {
            mouseEventLock.lock();
            Robot bot = new Robot();
            bot.mouseMove(x, y);
            bot.mousePress(InputEvent.BUTTON1_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_MASK);
            TimeUnit.MILLISECONDS.sleep(350);

        } catch (AWTException | InterruptedException e) {
            throw new InternalFailureException(e.getMessage());
        } finally {
            mouseEventLock.unlock();
        }
    }

    public static void moveMouseTo(int x,int y) {
        try {
            mouseEventLock.lock();
            Robot bot = new Robot();
            bot.mouseMove(x, y);

        } catch (AWTException e) {
            throw new InternalFailureException(e.getMessage());
        } finally {
            mouseEventLock.unlock();
        }
    }

    public static void moveMouseToStartingPoint() {
        moveMouseTo(0,0);
    }
}
