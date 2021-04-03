package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongPredicate;
import com.annimon.stream.function.LongUnaryOperator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        LongStream.of(12, 32, 22, 9, 30, 41, 42)
                .takeWhile(Functions.remainderLong(2))
                .custom(assertElements(arrayContaining(
                        12L, 32L, 22L
                )));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        LongStream.of(5, 32, 22, 9, 30, 41, 42)
                .takeWhile(Functions.remainderLong(2))
                .custom(assertIsEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        LongStream.of(10, 20, 30)
                .takeWhile(Functions.remainderLong(2))
                .custom(assertElements(arrayContaining(
                        10L, 20L, 30L
                )));
    }

    @Test
    public void testTakeWhileIterator() {
        PrimitiveIterator.OfLong it = LongStream.of(2, 4, 5, 6)
                .takeWhile(Functions.remainderLong(2))
                .iterator();
        assertThat(it.nextLong(), is(2L));
        assertThat(it.nextLong(), is(4L));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator2() {
        PrimitiveIterator.OfLong it = LongStream.of(1)
                .takeWhile(Functions.remainderLong(2))
                .iterator();
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator3() {
        final PrimitiveIterator.OfLong it = LongStream.of(2)
                .takeWhile(Functions.remainderLong(2))
                .iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.nextLong(), is(2L));
        assertThat(it.hasNext(), is(false));
        assertThrows(NoSuchElementException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                it.next();
            }
        });
    }

    @Test
    public void testTakeWhileAndMapIssue193() {
        boolean match = LongStream.of(42, 44, 46, 0, 48)
                .takeWhile(new LongPredicate() {
                    @Override
                    public boolean test(long value) {
                        return value != 0;
                    }
                })
                .map(new LongUnaryOperator() {
                    @Override
                    public long applyAsLong(long value) {
                        return value + 2;
                    }
                })
                .anyMatch(Functions.remainderLong(10));
        assertFalse(match);
    }
}
