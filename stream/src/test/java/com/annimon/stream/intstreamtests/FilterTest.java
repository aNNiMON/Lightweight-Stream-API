package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntFunction;
import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.function.IntUnaryOperator;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;

public final class FilterTest {

    @Test
    public void testFilter() {
        IntStream.rangeClosed(1, 10)
                .filter(Functions.remainderInt(2))
                .custom(assertElements(arrayContaining(
                        2, 4, 6, 8, 10
                )));

        assertEquals(0, IntStream.iterate(0, new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand + 1;
            }
        }).filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 0;
            }
        }).findFirst().getAsInt());
    }

    @Test
    public void testFilterWithMap() {
        final List<Integer> items = Arrays.asList(0, 1, 2, 3, 4);

        IntStream.range(0, items.size())
                .filter(new IntPredicate() {
                    @Override
                    public boolean test(int value) {
                        return items.get(value) % 2 == 0;
                    }
                })
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int value) {
                        return Integer.toString(value);
                    }
                })
                .custom(StreamMatcher.assertElements(contains(
                        "0", "2", "4"
                )));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterIteratorNextOnEmpty() {
        IntStream.empty()
                .filter(Functions.remainderInt(2))
                .iterator()
                .next();
    }
}
