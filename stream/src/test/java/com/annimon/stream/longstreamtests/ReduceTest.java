package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isEmpty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ReduceTest {

    @Test
    public void testReduceWithIdentity() {
        long result = LongStream.of(12, -3772, 3039, 19840, 100000)
                .reduce(0L, new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                });
        assertThat(result, is(119119L));
    }

    @Test
    public void testReduceWithIdentityOnEmptyStream() {
        long result = LongStream.empty()
                .reduce(1234567L, new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                });
        assertThat(result, is(1234567L));
    }

    @Test
    public void testReduce() {
        assertThat(LongStream.of(12, -3772, 3039, 19840, 100000)
                .reduce(new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                }), hasValue(119119L));
    }

    @Test
    public void testReduceOnEmptyStream() {
        assertThat(LongStream.empty().reduce(new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        }), isEmpty());
    }
}
