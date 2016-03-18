package com.annimon.stream.test;

import com.annimon.stream.Stream;
import static com.annimon.stream.test.StreamMatcher.elements;
import static com.annimon.stream.test.StreamMatcher.isEmpty;
import static com.annimon.stream.test.StreamMatcher.isNotEmpty;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

public class StreamMatcherTest {

    @Test
    public void testIsEmpty() {
        assertThat(Stream.empty(), isEmpty());
        assertThat(Stream.of(1, 2), isNotEmpty());
    }

    @Test
    public void testElements() {
        final Stream<Integer> stream = Stream.range(0, 5);
        final List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4);
        assertThat(stream, elements(is(expected)));
    }
}
