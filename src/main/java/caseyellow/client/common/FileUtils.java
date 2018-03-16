package caseyellow.client.common;

import caseyellow.client.exceptions.InternalFailureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static caseyellow.client.common.Utils.generateUniqueID;

@Slf4j
public class FileUtils {

    private static String tmpDirPath;

    static {
        try {
            tmpDirPath = createRootTmpDir();
        } catch (IOException e) {
            String errorMessage = String.format("Failed to create case yellow tmp dir, cause: %s", e.getMessage());
            log.error(errorMessage);
            throw new InternalFailureException(errorMessage, e);
        }
    }

    private static String createRootTmpDir() throws IOException {
        File rootTmpFile = new File(System.getProperty("java.io.tmpdir"), "case-yellow-tmp-dir");

        if (!rootTmpFile.exists()) {
            rootTmpFile.mkdir();
        } else {
            org.apache.commons.io.FileUtils.cleanDirectory(rootTmpFile);
        }

        return rootTmpFile.getAbsolutePath();
    }

    public static String takeScreenSnapshot() {
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            File screenshotFile = new File(createTmpDir(), "screenshot.png");
            ImageIO.write(capture, "png", screenshotFile);

            return screenshotFile.getAbsolutePath();
        } catch (Exception e) {
            throw new InternalFailureException(e.getMessage(), e);
        }
    }

    public static byte[] createImageBase64Encode(String imgPath) throws IOException {
        File imageFile = new File(imgPath);
        byte[] imageBase64Encode = Base64.getEncoder().encode(org.apache.commons.io.FileUtils.readFileToByteArray(imageFile));

        return imageBase64Encode;
    }

    public static String readFile(String path) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToString(new File(path), Charset.forName("UTF-8"));
    }

    public static File getSnapshotMetadataFile() {
        return new File("logs", "snapshotFile");
    }

    public static String getScreenResolution() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();

        return width + "_" + height;
    }

    public static File createTmpDir() {
        File tmpDir = new File(tmpDirPath, generateUniqueID());
        tmpDir.mkdirs();

        return tmpDir;
    }

    public static File getTempFileFromResources(String relativePath) throws IOException {
        Path path = Paths.get(relativePath);
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = new File(createTmpDir(), path.getFileName().toString());
        InputStream resourceAsStream = classLoader.getResourceAsStream(relativePath);
        byte[] bytes = IOUtils.toByteArray(resourceAsStream);
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, bytes);

        return file;
    }
}
