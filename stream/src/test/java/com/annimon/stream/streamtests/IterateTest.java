package com.annimon.stream.streamtests;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.UnaryOperator;
import java.math.BigInteger;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isPresent;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class IterateTest {

    @Test
    public void testIterate() {
        final BigInteger two = BigInteger.valueOf(2);
        BigInteger sum = Stream
                .iterate(BigInteger.ONE, new UnaryOperator<BigInteger>() {
                    @Override
                    public BigInteger apply(BigInteger value) {
                        return value.multiply(two);
                    }
                })
                .limit(100)
                .reduce(BigInteger.ZERO, new BinaryOperator<BigInteger>() {
                    @Override
                    public BigInteger apply(BigInteger value1, BigInteger value2) {
                        return value1.add(value2);
                    }
                });
        assertEquals(new BigInteger("1267650600228229401496703205375"), sum);
    }

    @Test(expected = NullPointerException.class)
    public void testIterateNull() {
        Stream.iterate(1, null);
    }

    @Test(timeout=2000)
    public void testIterateIssue53() {
        Optional<Integer> res = Stream
                .iterate(0, new UnaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer value) {
                        return value + 1;
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer value) {
                        return value == 0;
                    }
                })
                .findFirst();
        assertThat(res, isPresent());
        assertThat(res.get(), is(0));
    }

    @Test
    public void testIterateWithPredicate() {
        Predicate<Integer> condition = new Predicate<Integer>() {
            @Override
            public boolean test(Integer value) {
                return value < 20;
            }
        };
        UnaryOperator<Integer> increment = new UnaryOperator<Integer>() {
            @Override
            public Integer apply(Integer t) {
                return t + 5;
            }
        };
        Stream.iterate(0, condition, increment)
                .custom(assertElements(contains(
                        0, 5, 10, 15
                )));
    }
}
