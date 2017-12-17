package caseyellow.client.common;

import caseyellow.client.domain.analyze.model.Point;
import caseyellow.client.exceptions.InternalFailureException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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

    // Constants Variables
    private final static String tmpDirPath;
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    public static final String RESOLUTION_SEPARATOR = ";";

    private static ReentrantLock mouseEventLock;

    static {
        tmpDirPath = createRootTmpDir();
        mouseEventLock = new ReentrantLock();
    }

    private static String createRootTmpDir() {
        File rootTmpFile = new File(System.getProperty("java.io.tmpdir"), "case-yellow-tmp-dir");

        if (!rootTmpFile.exists()) {
            rootTmpFile.mkdir();
        }

        return rootTmpFile.getAbsolutePath();
    }
    // Helper functions

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    public static String formatDecimal(double value) {
        return String.format("%.2f", value);
    }

    public static String formatDate(Date date) {
        return dateFormatter.format(date);
    }

    public static File getTempFileFromResources(String relativePath) throws IOException {
        Path path = Paths.get(relativePath);
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = File.createTempFile(generateUniqueID(), path.getFileName().toString());
        InputStream resourceAsStream = classLoader.getResourceAsStream(relativePath);
        byte[] bytes = IOUtils.toByteArray(resourceAsStream);
        FileUtils.writeByteArrayToFile(file, bytes);

        return file;
    }

    public static File getFileFromResources(String relativePath) throws IOException, URISyntaxException {
        URL resource = Utils.class.getResource("/" + relativePath);
        return Paths.get(resource.toURI()).toFile();
    }

    public static String getImgFromResources(String relativePath) throws IOException {
        String screenResolution = getScreenResolution();
        ClassLoader classLoader = Utils.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(relativePath + RESOLUTION_SEPARATOR + screenResolution  + ".PNG");
        BufferedImage image = ImageIO.read(resourceAsStream);
        File tmpFile = File.createTempFile(generateUniqueID(), ".PNG");
        ImageIO.write(image, "PNG", tmpFile);

        return tmpFile.getAbsolutePath();
    }

    public static File createTmpFile(String fileExtension) {
        if (!fileExtension.startsWith(".")) {
            fileExtension = "." + fileExtension;
        }
        File tmpFile = new File(tmpDirPath, generateUniqueID() + fileExtension);

        return tmpFile;
    }

    public static File createTmpDir() {
        File tmpDir = new File(tmpDirPath, generateUniqueID());
        tmpDir.mkdir();

        return tmpDir;
    }

    public static String readFile(String path) throws IOException {
        return FileUtils.readFileToString(new File(path), Charset.forName("UTF-8"));
    }

    public static String getScreenResolution() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();

        return width + "_" + height;
    }

    public static String takeScreenSnapshot() {
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            File screenshotFile = new File(Utils.createTmpDir(), "screenshot.png");
            ImageIO.write(capture, "png", screenshotFile);

            return screenshotFile.getAbsolutePath();
        } catch (Exception e) {
            throw new InternalFailureException(e.getMessage(), e);
        }
    }

    public static byte[] createImageBase64Encode(String imgPath) throws IOException {
        File imageFile = new File(imgPath);
        byte[] imageBase64Encode = Base64.getEncoder().encode(FileUtils.readFileToByteArray(imageFile));

        return imageBase64Encode;
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
            TimeUnit.MILLISECONDS.sleep(400);

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

    public static caseyellow.client.domain.analyze.model.Point getCenter(List<caseyellow.client.domain.analyze.model.Point> vertices) {
        int minX = Utils.getMinX(vertices);
        int minY = Utils.getMinY(vertices);
        int maxX = Utils.getMaxX(vertices);
        int maxY = Utils.getMaxY(vertices);

        caseyellow.client.domain.analyze.model.Point center = new caseyellow.client.domain.analyze.model.Point( (minX + maxX)/2, (minY + maxY)/2);

        return center;
    }

    public static int getMinX(List<caseyellow.client.domain.analyze.model.Point> vertices) {
        return getMin(caseyellow.client.domain.analyze.model.Point::getX, vertices);
    }

    public static int getMinY(List<caseyellow.client.domain.analyze.model.Point> vertices) {
        return getMin(caseyellow.client.domain.analyze.model.Point::getY, vertices);
    }

    public static int getMaxX(List<caseyellow.client.domain.analyze.model.Point> vertices) {
        return getMax(caseyellow.client.domain.analyze.model.Point::getX, vertices);
    }

    public static int getMaxY(List<caseyellow.client.domain.analyze.model.Point> vertices) {
        return getMax(caseyellow.client.domain.analyze.model.Point::getY, vertices);
    }

    private static int getMin(ToIntFunction<? super caseyellow.client.domain.analyze.model.Point> intMinFunction, List<caseyellow.client.domain.analyze.model.Point> points) {

        return points.stream()
                     .mapToInt(intMinFunction)
                     .min()
                     .orElseThrow(() -> new InternalFailureException("There is no min point in points: " + points));
    }

    private static int getMax(ToIntFunction<? super caseyellow.client.domain.analyze.model.Point> intMaxFunction, List<Point> points) {

        return points.stream()
                     .mapToInt(intMaxFunction)
                     .max()
                     .orElseThrow(() -> new InternalFailureException("There is no max point in points: " + points));
    }
}
