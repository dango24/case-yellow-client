package caseyellow.client.common;


import caseyellow.client.exceptions.BrowserCommandFailedException;
import caseyellow.client.exceptions.InternalFailureException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Dan on 6/20/2017.
 */
public class Utils {

    // Constants Variables
    private final static String tmpDirPath;
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    public static final String RESOLUTION_SEPARATOR = ";";

    static {
        tmpDirPath = new File(System.getProperty("java.io.tmpdir"), "case-yellow-tmp-dir").getAbsolutePath();
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

    public static File createTmpDir() {
        File tmpDir = new File(tmpDirPath, generateUniqueID());
        tmpDir.mkdir();

        return tmpDir;
    }

    public static String getScreenResolution() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();

        return width + "_" + height;
    }

    public static File getSubImageFile(int x, int y , int w , int h, String screenshot) throws IOException {

        if (Objects.isNull(screenshot)) {
            throw new InternalFailureException("Screenshot is null");
        }

        File screenshotFile = new File(screenshot);
        File subImageFile = new File(createTmpDir(), "subImage.png");
        BufferedImage fullImg = ImageIO.read(screenshotFile);

        BufferedImage downloadRateScreenshot = fullImg.getSubimage(x, y, w, h);
        ImageIO.write(downloadRateScreenshot, "png", subImageFile);

        return subImageFile;
    }

    public static String takeScreenSnapshot() {
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            File screenshotFile = new File(Utils.createTmpDir(), "temp_screenshot.png");
            ImageIO.write(capture, "png", screenshotFile);

            return screenshotFile.getAbsolutePath();
        } catch (Exception e) {
            throw new InternalFailureException(e.getMessage(), e);
        }
    }

    public static void cleanTmpDir() throws IOException {
        FileUtils.deleteDirectory(new File(tmpDirPath));

        File tmpDir = new File(tmpDirPath);
        tmpDir.mkdir();
    }
}
