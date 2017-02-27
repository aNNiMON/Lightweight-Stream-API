package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class DistinctTest {

    @Test
    public void testDistinct() {
        assertTrue(IntStream.of(1, 2, -1, 10, 1, 1, -1, 5).distinct().count() == 5);
        assertTrue(IntStream.of(1, 2, -1, 10, 1, 1, -1, 5).distinct().sum() == 17);
    }

    @Test
    public void testDistinctLazy() {
        Integer[] expected = { 1, 2, 3, 5, -1 };

        List<Integer> input = new ArrayList<Integer>();
        input.addAll(Arrays.asList(1, 1, 2, 3, 5));
        IntStream stream = Stream.of(input)
                .mapToInt(Functions.toInt())
                .distinct();
        input.addAll(Arrays.asList(3, 2, 1, 1, -1));


        List<Integer> actual = stream.boxed().toList();
        assertThat(actual, Matchers.contains(expected));
    }
}
