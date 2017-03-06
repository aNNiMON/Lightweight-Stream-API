package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public final class SortedTest {

    @Test
    public void testSorted() {
        Stream.of(6, 3, 9, 0, -7, 19)
                .sorted()
                .custom(assertElements(contains(
                        -7, 0, 3, 6, 9, 19
                )));
    }

    @Test
    public void testSortedLazy() {
        List<Integer> input = new ArrayList<Integer>(6);
        input.addAll(Arrays.asList(6, 3, 9));
        Stream<Integer> stream = Stream.of(input).sorted();
        input.addAll(Arrays.asList(0, -7, 19));

        assertThat(stream, elements(contains(-7, 0, 3, 6, 9, 19)));
    }

    @Test
    public void testSortedWithComparator() {
        Stream.of(6, 3, 9, 0, -7, 19)
                .sorted(Functions.descendingAbsoluteOrder())
                .custom(assertElements(contains(
                        19, 9, -7, 6, 3, 0
                )));
    }
}
