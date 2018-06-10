package caseyellow.client.sevices.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartTestDetails {

    private String user;
    private String identifier;
    private List<String> urls;
}
