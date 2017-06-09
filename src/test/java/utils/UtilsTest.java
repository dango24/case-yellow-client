package utils;

import app.utils.SystemUtils;
import app.utils.Utils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

/**
 * Created by dango on 6/2/17.
 */
public class UtilsTest {

    @Autowired
    private Utils utils;

    @Test
    public void uniqueIdNotEmpty() throws Exception {
        String uniqueId = utils.generateUniqueID();
        assertFalse(uniqueId.isEmpty());
    }

    @Test
    public void generateUniqueIdIsUnique() throws Exception {
        Set<String> uniqueIdsSet;
        List<String> uniqueIdsList = IntStream.range(0, 1_000_000)
                                              .mapToObj(newId -> utils.generateUniqueID())
                                              .collect(toList());

        uniqueIdsSet = new HashSet<>(uniqueIdsList);

        assertEquals(uniqueIdsList.size(), uniqueIdsSet.size());
    }

    @Test
    public void generateUniqueIdWithNoDots() {
        assertFalse(utils.generateUniqueID().contains("."));
    }

    @Test
    public void getConnectionNotEmpty() throws Exception {
        String connectionType = SystemUtils.getConnection();
        assertFalse(connectionType.isEmpty());
    }

    @Test
    public void getConnectionIsConsist() throws Exception {

        List<String> uniqueIdsList = IntStream.range(0, 10)
                                              .mapToObj(newId -> SystemUtils.getConnection())
                                              .distinct()
                                              .collect(toList());

        assertTrue(uniqueIdsList.size() == 1);
    }

}