package caseyellow.client.sevices.central;

import caseyellow.client.App;
import caseyellow.client.domain.interfaces.DataAccessService;
import caseyellow.client.domain.test.model.ComparisonInfo;
import caseyellow.client.domain.test.model.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CentralServiceImpTest {

    private static final String DUMMY_TEST_LOCATION = "/tests/dummy_test.json";

    @Autowired
    private DataAccessService dataAccessService;

    @org.junit.Test
    public void saveTest() throws Exception {
        Path testPath = Paths.get(CentralServiceImpTest.class.getResource(DUMMY_TEST_LOCATION).toURI());
        Test test = new ObjectMapper().readValue(testPath.toFile(), Test.class);

        dataAccessService.saveTest(test);
    }

}