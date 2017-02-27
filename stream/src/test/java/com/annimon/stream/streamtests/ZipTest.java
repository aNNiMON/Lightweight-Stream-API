package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiFunction;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class ZipTest {

    @Test
    public void testZip() {
        Stream<Integer> shorter = Stream.rangeClosed(1, 5);
        Stream<Integer> longer = Stream.rangeClosed(1, 10);
        Stream.zip(
                shorter, longer,
                new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer value1, Integer value2) {
                        return value1 + value2;
                    }
                })
                .custom(assertElements(contains(
                      2, 4, 6, 8, 10
                )));
    }

    @Test(expected = NullPointerException.class)
    public void testZipNull1() {
        Stream.zip(null, Stream.<Integer>empty(), Functions.addition());
    }

    @Test(expected = NullPointerException.class)
    public void testZipNull2() {
        Stream.zip(Stream.<Integer>empty(), null, Functions.addition());
    }

    @Test
    public void testZipIterator() {
        List<Integer> shorter = Arrays.asList(1, 2, 3, 4, 5);
        Stream<Integer> longer = Stream.rangeClosed(1, 10);
        Stream.zip(
                shorter.iterator(), longer.iterator(),
                new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer value1, Integer value2) {
                        return value1 + value2;
                    }
                })
                .custom(assertElements(contains(
                      2, 4, 6, 8, 10
                )));
    }
}
