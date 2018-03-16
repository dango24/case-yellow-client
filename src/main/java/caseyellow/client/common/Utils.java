package caseyellow.client.common;

import caseyellow.client.domain.analyze.model.Point;
import caseyellow.client.exceptions.InternalFailureException;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

/**
 * Created by Dan on 6/20/2017.
 */
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

    public static Point getCenter(List<caseyellow.client.domain.analyze.model.Point> vertices) {
        int minX = Utils.getMinX(vertices);
        int minY = Utils.getMinY(vertices);
        int maxX = Utils.getMaxX(vertices);
        int maxY = Utils.getMaxY(vertices);

        Point center = new caseyellow.client.domain.analyze.model.Point( (minX + maxX)/2, (minY + maxY)/2);

        return center;
    }

    public static int getMinX(List<Point> vertices) {
        return getMin(caseyellow.client.domain.analyze.model.Point::getX, vertices);
    }

    public static int getMinY(List<Point> vertices) {
        return getMin(caseyellow.client.domain.analyze.model.Point::getY, vertices);
    }

    public static int getMaxX(List<Point> vertices) {
        return getMax(caseyellow.client.domain.analyze.model.Point::getX, vertices);
    }

    public static int getMaxY(List<Point> vertices) {
        return getMax(caseyellow.client.domain.analyze.model.Point::getY, vertices);
    }

    private static int getMin(ToIntFunction<? super Point> intMinFunction, List<Point> points) {

        return points.stream()
                     .mapToInt(intMinFunction)
                     .min()
                     .orElseThrow(() -> new InternalFailureException("There is no min point in points: " + points));
    }

    private static int getMax(ToIntFunction<? super Point> intMaxFunction, List<Point> points) {

        return points.stream()
                     .mapToInt(intMaxFunction)
                     .max()
                     .orElseThrow(() -> new InternalFailureException("There is no max point in points: " + points));
    }
}
