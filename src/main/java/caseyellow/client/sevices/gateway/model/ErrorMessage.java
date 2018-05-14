package caseyellow.client.sevices.gateway.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorMessage {

    private String timestamp;
    private String status;
    private String error;
    private String message;
    private String path;
}
