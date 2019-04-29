package caseyellow.client.domain.system;

import caseyellow.client.domain.logger.services.CYLogger;
import caseyellow.client.exceptions.IORuntimeException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class CommandExecutorServiceImpl implements CommandExecutorService {

    public static final String TIMEOUT_ERROR = "TIMEOUT_ERROR";
    private static CYLogger log = new CYLogger(CommandExecutorServiceImpl.class);

    private ExecutorService executorService;

    public CommandExecutorServiceImpl() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public String executeCommand(String command, int timeoutInMin) {
        try {
            log.info(String.format("Execute command: %s, with timeout of: %d minutes", command, timeoutInMin));
            Future<String> output = executorService.submit(() -> invokeExecuteCommand(command));

            return output.get(timeoutInMin, TimeUnit.MINUTES);

        }catch (TimeoutException e) {
            log.error(String.format("Reach time out, Failed to execute command: %s", e.getMessage()), e);
            return TIMEOUT_ERROR;

        } catch (Exception e) {
            log.error(String.format("Failed to execute command: %s", e.getMessage()), e);
            return "COMMAND_ERROR";
        }
    }

    private String invokeExecuteCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);

            return invokeExecuteInnerCommand(process);

        } catch (IOException e) {
            String errorMessage = String.format("Failed to execute command, cause: ", e.getMessage());
            log.error(errorMessage, e);

            throw new IORuntimeException(errorMessage, e);
        }
    }

    private String invokeExecuteInnerCommand(Process process) {
        try (InputStream inputStream = process.getInputStream()) {

            return IOUtils.toString(inputStream, UTF_8);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to read input data from inception service, cause: ", e.getMessage());
            log.error(errorMessage, e);

            throw new IORuntimeException(errorMessage, e);
        }
    }
}
