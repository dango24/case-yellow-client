package utils;

import app.caseyellow.client.domain.services.interfaces.SystemService;
import app.caseyellow.client.infrastructre.SystemServiceImp;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static app.caseyellow.client.common.Utils.generateUniqueID;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * Created by dango on 6/2/17.
 */
public class UtilsTest {

    @Test
    public void uniqueIdNotEmpty() throws Exception {
        String uniqueId = generateUniqueID();
        assertFalse(uniqueId.isEmpty());
    }

    @Test
    public void generateUniqueIdIsUnique() throws Exception {
        Set<String> uniqueIdsSet;
        List<String> uniqueIdsList = IntStream.range(0, 1_000_000)
                                              .mapToObj(newId -> generateUniqueID())
                                              .collect(toList());

        uniqueIdsSet = new HashSet<>(uniqueIdsList);

        assertEquals(uniqueIdsList.size(), uniqueIdsSet.size());
    }

    @Test
    public void generateUniqueIdWithNoDots() {
        assertFalse(generateUniqueID().contains("."));
    }

    @Test
    public void getConnectionNotEmpty() throws Exception {
        String connectionType = getConnection();
        assertFalse(connectionType.isEmpty());
    }

    @Test
    public void getConnectionIsConsist() throws Exception {

        List<String> uniqueIdsList = IntStream.range(0, 10)
                                              .mapToObj(newId -> getConnection())
                                              .distinct()
                                              .collect(toList());

        assertTrue(uniqueIdsList.size() == 1);
    }


    private String getConnection() {
        SystemService systemService;
        Method getConnectionMethod;

        try {
            systemService = new SystemServiceImp();
            getConnectionMethod = SystemServiceImp.class.getDeclaredMethod("getConnection");
            getConnectionMethod.setAccessible(true);
            return (String)getConnectionMethod.invoke(systemService, null);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return SystemService.UNKNOWN_CONNECTION;
        }
    }

}