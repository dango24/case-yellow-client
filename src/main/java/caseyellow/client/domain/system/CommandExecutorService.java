package caseyellow.client.domain.system;


public interface CommandExecutorService {

    String executeCommand(String command, int timeoutInMin);
}
