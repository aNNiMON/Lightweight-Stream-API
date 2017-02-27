package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public final class GroupByTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testGroupBy() {
        final Integer partitionItem = 1;

        List<Map.Entry<Boolean, List<Integer>>> result;
        result = Stream.of(1, 2, 3, 1, 2, 3, 1, 2, 3)
                .groupBy(Functions.equalityPartitionItem(partitionItem))
                .toList();
        
        assertThat(result, containsInAnyOrder(
                entry(false, Arrays.asList(2, 3, 2, 3, 2, 3)),
                entry(true, Arrays.asList(1, 1, 1))
        ));
    }

    private Map.Entry<Boolean, List<Integer>> entry(boolean key, List<Integer> value) {
        return new AbstractMap.SimpleEntry<Boolean, List<Integer>>(key, value);
    }
}
