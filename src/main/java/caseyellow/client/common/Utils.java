package caseyellow.client.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Dan on 6/20/2017.
 */
public class Utils {

    // Constants Variables
    public static final SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    // Helper functions

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
}
