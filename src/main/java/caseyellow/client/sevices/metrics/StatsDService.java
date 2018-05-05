package caseyellow.client.sevices.metrics;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

//@Service
public class StatsDService {

    @Value("${statsd.prefix}")
    private String statsDprefix;

    @Value("${statsd.host}")
    private String statsDHost;

    @Value("${statsd.port:8125}")
    private int statsDPort;

    private StatsDClient statsDClient;

    @PostConstruct
    private void init() {
        statsDClient = new NonBlockingStatsDClient(statsDprefix, statsDHost, statsDPort);
//        statsDClient.incrementCounter("bar");
        IntStream.range(0, 100).forEach( (i) -> statsDClient.count("test.dango", 10));
//        statsDClient.recordGaugeValue("baz", 100);
        statsDClient.count("test.dango", 10);
        statsDClient.count("test.dango", 10);
//        statsDClient.recordExecutionTime("bag", 25);
//        statsDClient.recordSetEvent("qux", "one");
    }
}
