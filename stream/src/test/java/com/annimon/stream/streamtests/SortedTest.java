package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SortedTest {

    @Test
    public void testSorted() {
        List<Integer> expected = Arrays.asList(-7, 0, 3, 6, 9, 19);
        Stream<Integer> stream = Stream.of(6, 3, 9, 0, -7, 19).sorted();
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testSortedLazy() {
        List<Integer> expected = Arrays.asList(-7, 0, 3, 6, 9, 19);

        List<Integer> input = new ArrayList<Integer>(6);
        input.addAll(Arrays.asList(6, 3, 9));
        Stream<Integer> stream = Stream.of(input).sorted();
        input.addAll(Arrays.asList(0, -7, 19));

        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testSortedWithComparator() {
        List<Integer> expected = Arrays.asList(19, 9, -7, 6, 3, 0);
        Stream<Integer> stream = Stream.of(6, 3, 9, 0, -7, 19)
                .sorted(Functions.descendingAbsoluteOrder());
        assertThat(stream, elements(is(expected)));
    }
}
