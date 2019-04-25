package caseyellow.client.domain.system;

import caseyellow.client.domain.logger.services.CYLogger;
import caseyellow.client.exceptions.IORuntimeException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class CommandExecutorServiceImpl implements CommandExecutorService {

    private static CYLogger log = new CYLogger(CommandExecutorServiceImpl.class);

    private ExecutorService executorService;

    public CommandExecutorServiceImpl() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public String executeCommand(String command) {
        try {
            Future<String> output = executorService.submit(() -> invokeExecuteCommand(command));

            return output.get(10, TimeUnit.MINUTES);

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
