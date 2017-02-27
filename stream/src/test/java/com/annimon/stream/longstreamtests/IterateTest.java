package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongPredicate;
import com.annimon.stream.function.LongUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class IterateTest {

    @Test
    public void testStreamIterate() {
        LongUnaryOperator operator = new LongUnaryOperator() {
            @Override
            public long applyAsLong(long operand) {
                return operand + 1000000;
            }
        };

        assertThat(LongStream.iterate(0L, operator).limit(4),
                elements(arrayContaining(0L, 1000000L, 2000000L, 3000000L))
        );
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
        LongStream stream = LongStream.iterate(0, condition, increment);

        assertThat(stream.toArray(), is(new long[] {0, 5, 10, 15}));
    }
}
