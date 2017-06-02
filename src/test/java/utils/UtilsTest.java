package utils;

import org.junit.Test;

import java.util.Collections;
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

    @Test
    public void uniqueIdNotEmpty() throws Exception {
        String uniqueId = Utils.generateUniqueID();
        assertFalse(uniqueId.isEmpty());
    }

    @Test
    public void generateUniqueIdIsUnique() throws Exception {
        Set<String> uniqueIdsSet;
        List<String> uniqueIdsList = IntStream.range(0, 1_000_000)
                                              .mapToObj(newId -> Utils.generateUniqueID())
                                              .collect(toList());

        uniqueIdsSet = new HashSet<>(uniqueIdsList);

        assertEquals(uniqueIdsList.size(), uniqueIdsSet.size());
    }

    @Test
    public void getConnectionNotEmpty() throws Exception {
        String connectionType = Utils.getConnection();
        System.out.println(connectionType);
        assertFalse(connectionType.isEmpty());
    }

    @Test
    public void getConnectionIsConsist() throws Exception {

        List<String> uniqueIdsList = IntStream.range(0, 10)
                                              .mapToObj(newId -> Utils.getConnection())
                                              .distinct()
                                              .collect(toList());

        assertTrue(uniqueIdsList.size() == 1);
    }

}