package caseyellow.client.exceptions;

/**
 * Created by dango on 6/9/17.
 */
public class FileDownloadInfoException extends RuntimeException {

    public FileDownloadInfoException(String messgae) {
        super(messgae);
    }

    public FileDownloadInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
