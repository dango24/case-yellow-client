package app.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by dango on 6/2/17.
 */
public class Utils {

    // Logger
    private static Logger log;

    // Constants Variables
    public final static SimpleDateFormat formatter;

    // Init
    static {
        formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        log = Logger.getLogger(Utils.class);
    }

    // Utilities functions

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    public static double round(double value, int places) {

        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);

        return (double) tmp / factor;
    }

    public static String format(Date date) {
        return formatter.format(date);
    }

    public List<String> readFile(String path) {
        return utils.Utils.readFile(Utils.class.getResourceAsStream(path).toString());
    }

    public static String getProperty(String key) {
        Properties props = getApplicationProperties();
        return props.getProperty(key);
    }

    public static void changeProperty(String key, String value) {
        Properties props = getApplicationProperties();
        props.setProperty(key, value);
    }

    private static Properties getApplicationProperties() {
        Properties props = new Properties();

        try (InputStream configStream = Utils.class.getResourceAsStream( "/application.properties")) {
            props.load(configStream);

        } catch (IOException e) {
            System.out.println("Error: failed to load log4j configuration file");
        }
        return props;
    }

}
