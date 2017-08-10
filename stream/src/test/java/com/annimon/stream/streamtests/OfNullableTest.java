package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.StreamMatcher.isEmpty;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public final class OfNullableTest {

    @Test
    public void testStreamOfNullable() {
        String t = null;
        assertThat(Stream.ofNullable(t), isEmpty());

        assertThat(Stream.ofNullable(5), elements(contains(5)));
    }

    @Test
    public void testStreamOfNullableArray() {
        String[] t = null;
        assertThat(Stream.ofNullable(t), isEmpty());

        assertThat(Stream.ofNullable(new Integer[] { 1, 2, 3 }), elements(contains(1, 2, 3)));
    }

    @Test
    public void testStreamOfNullableMap() {
        Map<Integer, String> t = null;
        assertThat(Stream.ofNullable(t), isEmpty());

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1, 2);
        map.put(3, 4);
        Stream.ofNullable(map)
                .flatMap(new Function<Map.Entry<Integer, Integer>, Stream<Integer>>() {
                    @Override
                    public Stream<Integer> apply(Map.Entry<Integer, Integer> e) {
                        return Stream.of(e.getKey(), e.getValue());
                    }
                })
                .custom(assertElements(contains(1, 2, 3, 4)));
    }

    @Test
    public void testStreamOfNullableWithIterator() {
        assertThat(Stream.ofNullable((Iterator<?>) null), isEmpty());

        assertThat(Stream.ofNullable(Arrays.asList(5, 10, 15).iterator()),
                elements(contains(5, 10, 15)));
    }

    @Test
    public void testStreamOfNullableWithIterable() {
        assertThat(Stream.ofNullable((List<?>) null), isEmpty());

        assertThat(Stream.ofNullable(Arrays.asList(5, 10, 15)),
                elements(contains(5, 10, 15)));
    }
}
