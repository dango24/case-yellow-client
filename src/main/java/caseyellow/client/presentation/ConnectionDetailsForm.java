package caseyellow.client.presentation;

import java.util.List;
import java.util.Map;

public interface ConnectionDetailsForm {
    void view(Map<String, List<String>> connectionDetail);
    void close();
}
