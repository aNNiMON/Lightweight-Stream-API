package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class ConcatTest {

    @Test
    public void testConcat() {
        Stream<String> stream1 = Stream.of("a", "b", "c", "d");
        Stream<String> stream2 = Stream.of("e", "f", "g", "h");
        Stream.concat(stream1, stream2)
                .custom(assertElements(contains(
                        "a", "b", "c", "d",
                        "e", "f", "g", "h"
                )));
    }

    @Test(expected = NullPointerException.class)
    public void testConcatNull1() {
        Stream.concat(null, Stream.empty());
    }

    @Test(expected = NullPointerException.class)
    public void testConcatNull2() {
        Stream.concat(Stream.empty(), null);
    }

    @Test
    public void testConcatOfFilter() {
        Stream<Integer> stream1 = Stream.range(0, 5).filter(Functions.remainder(1));
        Stream<Integer> stream2 = Stream.range(5, 10).filter(Functions.remainder(1));
        Stream.concat(stream1, stream2)
                .custom(assertElements(contains(
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9
                )));
    }

    @Test
    public void testConcatOfFlatMap() {
        final Function<Integer, Stream<Integer>> flatmapFunc = new Function<Integer, Stream<Integer>>() {
            @Override
            public Stream<Integer> apply(Integer value) {
                return Stream.of(value, value);
            }
        };
        Stream<Integer> stream1 = Stream.range(1, 3).flatMap(flatmapFunc); // 1122
        Stream<Integer> stream2 = Stream.range(3, 5).flatMap(flatmapFunc); // 3344
        Stream.concat(stream1, stream2)
                .custom(assertElements(contains(
                        1, 1, 2, 2, 3, 3, 4, 4
                )));
    }

    @Test
    public void testMergeIterator() {
        List<Integer> shorter = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> longer = Stream.rangeClosed(2, 8).toList();
        Stream.concat(shorter.iterator(), longer.iterator())
                .custom(assertElements(contains(
                        1, 2, 3, 4, 5, 2, 3, 4, 5, 6, 7, 8
                )));
        Stream.concat(longer.iterator(), shorter.iterator())
                .custom(assertElements(contains(
                        2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5
                )));
    }
}
