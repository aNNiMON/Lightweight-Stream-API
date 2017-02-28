package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.containsInAnyOrder;

public final class GroupByTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testGroupBy() {
        final Integer partitionItem = 1;

        Stream.of(1, 2, 3, 1, 2, 3, 1, 2, 3)
                .groupBy(Functions.equalityPartitionItem(partitionItem))
                .custom(assertElements(containsInAnyOrder(
                        entry(false, Arrays.asList(2, 3, 2, 3, 2, 3)),
                        entry(true, Arrays.asList(1, 1, 1))
                )));
    }

    private Map.Entry<Boolean, List<Integer>> entry(boolean key, List<Integer> value) {
        return new AbstractMap.SimpleEntry<Boolean, List<Integer>>(key, value);
    }
}
