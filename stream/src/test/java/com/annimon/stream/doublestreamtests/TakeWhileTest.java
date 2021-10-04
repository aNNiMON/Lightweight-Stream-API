package com.annimon.stream.doublestreamtests;

import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import com.annimon.stream.function.DoubleUnaryOperator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        DoubleStream.of(10.2, 30.234, 10.09, 2.2, 80d)
                .takeWhile(Functions.greaterThan(Math.PI))
                .custom(assertElements(arrayContaining(10.2, 30.234, 10.09)));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d)
                .takeWhile(Functions.greaterThan(Math.PI))
                .custom(assertIsEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        DoubleStream.of(10.2, 30.234, 10.09)
                .takeWhile(Functions.greaterThan(Math.PI))
                .custom(assertElements(arrayContaining(10.2, 30.234, 10.09)));
    }

    @Test
    public void testTakeWhileIterator() {
        PrimitiveIterator.OfDouble it =
                DoubleStream.of(5, 8.2, 1.19, 120)
                        .takeWhile(Functions.greaterThan(Math.PI))
                        .iterator();
        assertThat(it.nextDouble(), is(closeTo(5, 0.001)));
        assertThat(it.nextDouble(), is(closeTo(8.2, 0.001)));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator2() {
        PrimitiveIterator.OfDouble it =
                DoubleStream.of(1).takeWhile(Functions.greaterThan(2)).iterator();
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator3() {
        final PrimitiveIterator.OfDouble it =
                DoubleStream.of(5).takeWhile(Functions.greaterThan(2)).iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.nextDouble(), is(closeTo(5, 0.001)));
        assertThat(it.hasNext(), is(false));
        assertThrows(
                NoSuchElementException.class,
                new ThrowingRunnable() {
                    @Override
                    public void run() throws Throwable {
                        it.next();
                    }
                });
    }

    @Test
    public void testTakeWhileAndMapIssue193() {
        boolean match =
                DoubleStream.of(42, 44, 46, 0, 48)
                        .takeWhile(Functions.greaterThan(1))
                        .map(
                                new DoubleUnaryOperator() {
                                    @Override
                                    public double applyAsDouble(double value) {
                                        return value + 2;
                                    }
                                })
                        .anyMatch(Functions.greaterThan(100));
        assertFalse(match);
    }
}
