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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static caseyellow.client.common.Utils.generateUniqueID;
import static java.util.Objects.nonNull;

@Slf4j
public class FileUtils {

    private static String tmpDirPath;
    private static String driversDirPath;

    static {
        tmpDirPath = createRootTmpDir();
        driversDirPath = new File("bin", "drivers").getAbsolutePath();
    }

    private static String createRootTmpDir() {
        File rootTmpFile = new File(System.getProperty("java.io.tmpdir"), "case-yellow-tmp-dir");

        if (!rootTmpFile.exists()) {
            rootTmpFile.mkdir();
        } else {
            cleanDirectory(rootTmpFile);
        }

        return rootTmpFile.getAbsolutePath();
    }

    private static void cleanDirectory(File file) {
        try {
            org.apache.commons.io.FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            String errorMessage = String.format("Failed to clean case yellow tmp dir, cause: %s", e.getMessage());
            log.error(errorMessage);
        }
    }

    public static void cleanRootDir() {
        cleanDirectory(new File(tmpDirPath));
    }

    public static File takeScreenSnapshot() {
        File screenshotFile = null;

        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = new Robot().createScreenCapture(screenRect);
            screenshotFile = new File(createTmpDir(), "screenshot.png");
            ImageIO.write(capture, "png", screenshotFile);
            log.info(String.format("Create snapshot: %s", screenshotFile.getAbsolutePath()));

            return screenshotFile;

        } catch (Exception e) {
            deleteFile(screenshotFile);
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

    public static File createTmpDir() {
        File tmpDir = new File(tmpDirPath, generateUniqueID());
        tmpDir.mkdirs();

        return tmpDir;
    }

    public static File getFileFromResources(File file) throws IOException {
        return getFileFromResources(file.getAbsolutePath());
    }

    public static File getFileFromResources(String relativePath) throws IOException {
        return getFileFromResources(createTmpDir(), relativePath);
    }

    public static File getFileFromResources(File rootDir, String relativePath) throws IOException {
        Path path = Paths.get(relativePath);
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = new File(rootDir, path.getFileName().toString());
        InputStream resourceAsStream = classLoader.getResourceAsStream(relativePath);
        byte[] bytes = IOUtils.toByteArray(resourceAsStream);
        org.apache.commons.io.FileUtils.writeByteArrayToFile(file, bytes);

        return file;
    }

    public static File getDriverFromResources(String path) throws IOException {
        File file = new File(driversDirPath, path);

        if (file.exists()) {
            return file;
        }

        File tmpFile = getFileFromResources("drivers/" + path);
        org.apache.commons.io.FileUtils.copyFileToDirectory(tmpFile, file.getParentFile());
        FileUtils.deleteFile(tmpFile);

        return file;
    }

    public static void makeFileExecutable(String filePath) {
        File file = new File(filePath);

        file.setReadable(true, false);
        file.setWritable(false, true);
        file.setExecutable(true, false);
    }

    public static void deleteFile(String path) {
        if (nonNull(path)) {
            deleteFile(new File(path));
        }
    }

    public static void deleteFile(File file) {
        try {
            if (nonNull(file) && file.exists()) {

                if (file.isDirectory()) {
                    org.apache.commons.io.FileUtils.deleteDirectory(file);
                } else {
                    Files.deleteIfExists(file.toPath());
                }
            }
        } catch (IOException e) {
            log.error(String.format("Failed to delete file: %s", e.getMessage()));
        }
    }
}
