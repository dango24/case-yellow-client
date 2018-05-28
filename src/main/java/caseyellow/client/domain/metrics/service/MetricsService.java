package caseyellow.client.domain.metrics.service;

import caseyellow.client.domain.test.model.Test;

public interface MetricsService {
    void testStart(String identifier);
    void testFailed(String identifier);
    void testEndSuccessfully(String identifier);
    void testDuration(Test test);
}
