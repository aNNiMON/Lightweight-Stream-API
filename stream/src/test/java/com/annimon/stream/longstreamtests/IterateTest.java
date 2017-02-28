package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongPredicate;
import com.annimon.stream.function.LongUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class IterateTest {

    @Test
    public void testStreamIterate() {
        LongUnaryOperator operator = new LongUnaryOperator() {
            @Override
            public long applyAsLong(long operand) {
                return operand + 1000000;
            }
        };

        LongStream.iterate(0L, operator)
                .limit(4)
                .custom(assertElements(arrayContaining(
                        0L, 1000000L, 2000000L, 3000000L
                )));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamIterateNull() {
        LongStream.iterate(0, null);
    }

    @Test
    public void testStreamIterateWithPredicate() {
        LongPredicate condition = new LongPredicate() {
            @Override
            public boolean test(long value) {
                return value < 20;
            }
        };
        LongUnaryOperator increment = new LongUnaryOperator() {
            @Override
            public long applyAsLong(long t) {
                return t + 5;
            }
        };
        LongStream.iterate(0, condition, increment)
                .custom(assertElements(arrayContaining(
                        0L, 5L, 10L, 15L
                )));
    }
}
