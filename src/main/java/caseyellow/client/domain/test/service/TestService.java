package caseyellow.client.domain.test.service;

import caseyellow.client.domain.test.model.Test;
import caseyellow.client.exceptions.FileDownloadInfoException;
import caseyellow.client.exceptions.RequestFailureException;
import caseyellow.client.exceptions.UserInterruptException;

import java.io.IOException;

public interface TestService {
    Test generateNewTest() throws UserInterruptException, FileDownloadInfoException, RequestFailureException;
    void stop() throws IOException;
}
