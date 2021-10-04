package com.annimon.stream.streamtests;

import static com.annimon.stream.test.hamcrest.OptionalMatcher.isPresent;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.UnaryOperator;
import com.annimon.stream.test.hamcrest.OptionalMatcher;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public final class IterateTest {

    @Test
    public void testIterate() {
        final BigInteger two = BigInteger.valueOf(2);
        BigInteger sum =
                Stream.iterate(
                                BigInteger.ONE,
                                new UnaryOperator<BigInteger>() {
                                    @Override
                                    public BigInteger apply(BigInteger value) {
                                        return value.multiply(two);
                                    }
                                })
                        .limit(100)
                        .reduce(
                                BigInteger.ZERO,
                                new BinaryOperator<BigInteger>() {
                                    @Override
                                    public BigInteger apply(BigInteger value1, BigInteger value2) {
                                        return value1.add(value2);
                                    }
                                });
        assertEquals(new BigInteger("1267650600228229401496703205375"), sum);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void testIterateNull() {
        Stream.iterate(1, null);
    }

    @Test(timeout = 2000)
    public void testIterateIssue53() {
        Optional<Integer> res =
                Stream.iterate(
                                0,
                                new UnaryOperator<Integer>() {
                                    @Override
                                    public Integer apply(Integer value) {
                                        return value + 1;
                                    }
                                })
                        .filter(
                                new Predicate<Integer>() {
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
        Predicate<Integer> condition =
                new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer value) {
                        return value < 20;
                    }
                };
        UnaryOperator<Integer> increment =
                new UnaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer t) {
                        return t + 5;
                    }
                };
        Stream.iterate(0, condition, increment).custom(assertElements(contains(0, 5, 10, 15)));
    }

    @Test
    public void testIterateIssue186() {
        AtomicInteger result =
                Stream.iterate(
                                new AtomicInteger(0),
                                new Predicate<AtomicInteger>() {
                                    @Override
                                    public boolean test(AtomicInteger s) {
                                        return s.incrementAndGet() < 3;
                                    }
                                },
                                UnaryOperator.Util.<AtomicInteger>identity())
                        .findFirst()
                        .orElseThrow();
        assertEquals(1, result.get());
    }

    @Test
    public void testIterateIssue186OnEmptyStream() {
        Optional<AtomicInteger> result =
                Stream.iterate(
                                new AtomicInteger(0),
                                new Predicate<AtomicInteger>() {
                                    @Override
                                    public boolean test(AtomicInteger s) {
                                        return s.incrementAndGet() < 0;
                                    }
                                },
                                UnaryOperator.Util.<AtomicInteger>identity())
                        .findFirst();
        assertThat(result, OptionalMatcher.isEmpty());
    }
}
