package caseyellow.client.common;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static caseyellow.client.common.Utils.generateUniqueID;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Dan on 6/24/2017.
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
                                              .mapToObj(newId -> generateUniqueID())
                                              .collect(toList());

        uniqueIdsSet = new HashSet<>(uniqueIdsList);

        assertEquals(uniqueIdsList.size(), uniqueIdsSet.size());
    }

    @Test
    public void formatDecimal() throws Exception {

        List<Double> doubles = Arrays.asList(2.0, 0.0, 12225.543543543, 2434324324324.0, 6546542.24, 0.1);

        assertTrue(
                doubles.stream()
                       .map(Utils::formatDecimal)
                       .map(formattedDecimal -> formattedDecimal.split("\\.")[1])
                       .allMatch(formattedDecimalSuffix -> formattedDecimalSuffix.length() == 2) // get the double value that is only two digit after decimal point
        );
    }

}