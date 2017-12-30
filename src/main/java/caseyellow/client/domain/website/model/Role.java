package caseyellow.client.domain.website.model;

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public boolean isMono() {
        return mono;
    }

    public void setMono(boolean mono) {
        this.mono = mono;
    }

    public void done() {
        this.executed = true;
    }

    @Override
    public String toString() {
        return "Role{" +
                "identifier='" + identifier + '\'' +
                ", command=" + command +
                ", executed=" + executed +
                ", mono=" + mono +
                '}';
    }
}
