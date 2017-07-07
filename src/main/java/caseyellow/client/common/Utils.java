package caseyellow.client.common;


import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Dan on 6/20/2017.
 */
public class Utils {

    // Constants Variables
    private final static String tmpDirPath = System.getProperty("java.io.tmpdir");
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    public static final String RESOLUTION_SEPERATOR = ";";

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

    public static String getImgFromResources(String relativePath) {
        String screenResolution = getScreenResolution();
        ClassLoader classLoader = Utils.class.getClassLoader();
        try {
            InputStream resourceAsStream = classLoader.getResourceAsStream(relativePath + RESOLUTION_SEPERATOR + screenResolution  + ".PNG");
            BufferedImage image = ImageIO.read(resourceAsStream);
            File tmpFile = File.createTempFile(generateUniqueID(), ".PNG");
            ImageIO.write(image, "PNG", tmpFile);
            return tmpFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("dango");
        }

        return null;
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
}
