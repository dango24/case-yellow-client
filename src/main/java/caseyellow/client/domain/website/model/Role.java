package caseyellow.client.domain.website.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {

    private String identifier;
    private Command command;
    private boolean executed;
    private boolean mono;

    public Role() {
        this.executed = false;
    }

    public Role(String identifier, Command command, boolean mono) {
        this.identifier = identifier;
        this.command = command;
        this.mono = mono;
        this.executed = false;
    }

    public void done() {
        this.executed = true;
    }
}
