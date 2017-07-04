package caseyellow.client.infrastructre;

import caseyellow.client.domain.interfaces.SystemService;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Dan on 6/24/2017.
 */
public class SystemServiceImplTest {

    @Test
    public void getOperationSystemNotEmpty() throws Exception{
        String os = getReflectServiceMethod("getOperationSystem");
        assertFalse(os.isEmpty());
    }

    @Test
    public void getOperationSystemIsConsist() throws Exception{
        assertTrue(isMethodResultConsist("getOperationSystem"));
    }

    @Test
    public void getIpAddressNotEmpty() throws Exception{
        String publicIpAddress = getReflectServiceMethod("getPublicIPAddress");
        assertFalse(publicIpAddress.isEmpty());
    }

    @Test
    public void getIpAddressIsConsist() throws Exception {
        assertTrue(isMethodResultConsist("getPublicIPAddress"));
    }

    @Test
    public void getConnectionNotEmpty() throws Exception {
        String connectionType = getReflectServiceMethod("getConnection");
        assertFalse(connectionType.isEmpty());
    }

    @Test
    public void getConnectionIsConsist() throws Exception {
        assertTrue(isMethodResultConsist("getConnection"));
    }

    private String getReflectServiceMethod(String methodIdentifier) {
        SystemService systemService;
        Method getConnectionMethod;

        try {
            systemService = new SystemServiceImpl();
            getConnectionMethod = SystemServiceImpl.class.getDeclaredMethod(methodIdentifier);
            getConnectionMethod.setAccessible(true);
            return (String)getConnectionMethod.invoke(systemService);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return SystemService.UNKNOWN_CONNECTION;
        }
    }

    private boolean isMethodResultConsist(String methodIdentifier) throws Exception {

        List<String> uniqueIdsList = IntStream.range(0, 10)
                                              .mapToObj(newId -> getReflectServiceMethod(methodIdentifier))
                                              .distinct()
                                              .collect(toList());

        return uniqueIdsList.size() == 1;
    }
}