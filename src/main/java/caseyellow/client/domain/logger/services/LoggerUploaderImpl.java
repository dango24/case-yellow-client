package caseyellow.client.domain.logger.services;

import caseyellow.client.sevices.gateway.services.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Service("loggerUploaderImpl")
public class LoggerUploaderImpl implements LoggerUploader{

    private static CYLogger logger = new CYLogger(LoggerUploaderImpl.class);

    @Value("${log_dir_path}")
    private String logDirPath;

    private DataAccessService dataAccessService;

    @Autowired
    public LoggerUploaderImpl(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @Override
    public void uploadLogs() {
        CompletableFuture.runAsync( () -> uploadLogsFiles() );
    }

    private void uploadLogsFiles() {
        try {
            String logDirFullPath = new File(logDirPath).getAbsolutePath();
            String user = dataAccessService.getUser();

            Files.list(Paths.get(logDirFullPath))
                 .filter(Files::isRegularFile)
                 .forEach(path -> dataAccessService.uploadFileToServer(buildLogKey(user, path.getFileName()), path.toString()));

        } catch (Exception e) {
            logger.error("failed to upload log file to s3" + e.getMessage(), e);
        }
    }

    private String buildLogKey(String user, Path path) {
        return String.format("%s%s/%s", logDirPath, user, path.getFileName());
    }

}
