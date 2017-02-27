package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class SkipTest {

    @Test
    public void testSkip() {
        Stream.range(0, 10)
                .skip(7)
                .custom(assertElements(contains(
                        7, 8, 9
                )));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() {
        Stream.range(0, 10).skip(-2).count();
    }

    @Test
    public void testSkipZero() {
        Stream.range(0, 2)
                .skip(0)
                .custom(assertElements(contains(
                        0, 1
                )));
    }

    @Test
    public void testSkipMoreThanCount() {
        Stream.range(0, 10)
                .skip(15)
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }

    @Test
    public void testSkipLazy() {
        final List<Integer> data = new ArrayList<Integer>(10);
        data.add(0);

        Stream<Integer> stream = Stream.of(data).skip(3);
        data.addAll(Arrays.asList(1, 2, 3, 4, 5));
        stream.custom(assertElements(contains(
                3, 4, 5
        )));
    }
    
    
    @Test
    public void testSkipAndLimit() {
        Stream.range(0, 10)
                .skip(2)  // 23456789
                .limit(5) // 23456
                .custom(assertElements(contains(
                        2, 3, 4, 5, 6
                )));
    }

    @Test
    public void testLimitAndSkip() {
        Stream.range(0, 10)
                .limit(5) // 01234
                .skip(2)  // 234
                .custom(assertElements(contains(
                        2, 3, 4
                )));
    }

    @Test
    public void testSkipAndLimitMoreThanCount() {
        Stream.range(0, 10)
                .skip(8)   // 89
                .limit(15) // 89
                .custom(assertElements(contains(
                        8, 9
                )));
    }

    @Test
    public void testSkipMoreThanCountAndLimit() {
        Stream.range(0, 10)
                .skip(15)
                .limit(8)
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }

    @Test
    public void testSkipAndLimitTwice() {
        Stream.range(0, 10)
                .skip(2)  // 23456789
                .limit(5) // 23456
                .skip(2)  // 456
                .limit(2) // 45
                .custom(assertElements(contains(
                        4, 5
                )));
    }
}
