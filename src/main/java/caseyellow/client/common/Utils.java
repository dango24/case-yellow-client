package caseyellow.client.common;

import caseyellow.client.infrastructre.SystemServiceImpl;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Dan on 6/20/2017.
 */
public class Utils {

    // Constants Variables
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

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

    public static File getFileFromResources(String relativePath) {
        ClassLoader classLoader = Utils.class.getClassLoader();

        return new File(classLoader.getResource(relativePath).getFile());
    }

    public String readFile(String path) throws IOException {
        InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(path);

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
}
