package caseyellow.client.domain.logger.services;

import caseyellow.client.domain.logger.model.LogData;
import caseyellow.client.sevices.gateway.services.DataAccessService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static caseyellow.client.common.Utils.splitClassName;

public class CYLogger  {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DataAccessService dataAccessService;

    private Logger log;
    private String className;

    public CYLogger(Class clazz) {
        this.className = splitClassName(clazz.getName());
        this.log = Logger.getLogger(clazz);
    }

    public void debug(String message) {
        log.debug(adaptMessage(message));
        uploadLogData(message, Level.DEBUG);
    }

    public void debug(String message, Throwable t) {
        log.debug(adaptMessage(message), t);
        uploadLogData(message + "\n" + ExceptionUtils.getStackTrace(t), Level.DEBUG);
    }

    public void info(String message) {
        log.info(adaptMessage(message));
        uploadLogData(message, Level.INFO);
    }

    public void info(String message, Throwable t) {
        log.info(adaptMessage(message), t);
        uploadLogData(message + "\n" + ExceptionUtils.getStackTrace(t), Level.INFO);
    }

    public void warn(String message) {
        log.warn(adaptMessage(message));
        uploadLogData(message, Level.WARN);
    }

    public void warn(String message, Throwable t) {
        log.warn(adaptMessage(message), t);
        uploadLogData(message + "\n" + ExceptionUtils.getStackTrace(t), Level.WARN);
    }

    public void error(String message) {
        log.error(adaptMessage(message));
        uploadLogData(message, Level.ERROR);
    }

    public void error(String message, Throwable t) {
        log.error(adaptMessage(message), t);
        uploadLogData(message + "\n" + ExceptionUtils.getStackTrace(t), Level.ERROR);
    }

    private String adaptMessage(String message) {
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        return String.format(":%d - %s", lineNumber, message);
    }

    private void uploadLogData(String message, Level level) {
        String thread = Thread.currentThread().getName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        String correlationId = MDC.get("correlation-id");

        CompletableFuture.runAsync( () -> uploadLogData(message, level, thread, lineNumber, correlationId));
    }

    private void uploadLogData(String message, Level level, String thread, int lineNumber, String correlationId) {
        String date = DATE_FORMAT.format(new Date());
        String clazz = String.format("%s:%s", className, lineNumber);
        String userName = dataAccessService.getUser();
        String clientVersion = dataAccessService.clientVersion();

        LogData logData =
                LogData.logDataBuilder()
                       .user(userName)
                       .version(clientVersion)
                       .date(date)
                       .thread(thread)
                       .level(level.toString())
                       .classL(clazz)
                       .correlationId(correlationId)
                       .message(message)
                       .build();

        dataAccessService.uploadLogData(logData);
    }

    public static void setDataAccessService(DataAccessService dataAccessService) {
        CYLogger.dataAccessService = dataAccessService;
    }
}
