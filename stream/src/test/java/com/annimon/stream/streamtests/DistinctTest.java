package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.StreamMatcher.isEmpty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class DistinctTest {

    @Test
    public void testDistinct() {
        List<Integer> expected = Arrays.asList(-1, 1, 2, 3, 5);
        Stream<Integer> stream = Stream.of(1, 1, 2, 3, 5, 3, 2, 1, 1, -1)
                .distinct()
                .sorted();
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testDistinctEmpty() {
        Stream<Integer> stream = Stream.<Integer>empty().distinct();
        assertThat(stream, isEmpty());
    }

    @Test
    public void testDistinctPreservesOrder() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 5, -1);
        Stream<Integer> stream = Stream.of(1, 1, 2, 3, 5, 3, 2, 1, 1, -1)
                .distinct();
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testDistinctLazy() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 5, -1);

        List<Integer> input = new ArrayList<Integer>(10);
        input.addAll(Arrays.asList(1, 1, 2, 3, 5));
        Stream<Integer> stream = Stream.of(input).distinct();
        input.addAll(Arrays.asList(3, 2, 1, 1, -1));

        assertThat(stream, elements(is(expected)));
    }
}
