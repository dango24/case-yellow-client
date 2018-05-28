package caseyellow.client.domain.system;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class SystemServiceImplTest {

    private SystemServiceImpl systemService;

    @Before
    public void setUp() throws Exception {
        systemService = new SystemServiceImpl(null);
    }

//    @Test
    public void connectionTest() throws Exception {
        Method getConnectionMethod = SystemServiceImpl.class.getDeclaredMethod("getConnection");
        getConnectionMethod.setAccessible(true);

        getConnectionMethod.invoke(systemService);
    }
}