package caseyellow.client.presentation;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;


public class ConnectionDetailsFormImplTest {

    @Test
    public void view() throws Exception {
        ConnectionDetailsFormImpl connectionDetailsForm = new ConnectionDetailsFormImpl(null);
        Map<String, List<String>> map = new HashMap<>();
        List<String> speed = Arrays.asList("15", "40", "100", "200");
        List<String> infrastructure = Arrays.asList("HOT", "BEZEQ", "UNLIMITED", "PARTNER");
        map.put("speed", speed);
        map.put("infrastructure", infrastructure);

        connectionDetailsForm.view(map);

//        Thread.sleep(40000);
    }

}