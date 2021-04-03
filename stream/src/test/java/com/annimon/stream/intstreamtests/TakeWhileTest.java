package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.function.IntUnaryOperator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertIsEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainderInt(2))
                .custom(assertElements(arrayContaining(
                        2, 4, 6
                )));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainderInt(3))
                .custom(assertIsEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainderInt(1))
                .custom(assertElements(arrayContaining(
                        2, 4, 6, 7, 8, 10, 11
                )));
    }

    @Test
    public void testTakeWhileIterator() {
        PrimitiveIterator.OfInt it = IntStream.of(2, 4, 5, 6)
                .takeWhile(Functions.remainderInt(2))
                .iterator();
        assertThat(it.nextInt(), is(2));
        assertThat(it.nextInt(), is(4));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator2() {
        PrimitiveIterator.OfInt it = IntStream.of(1)
                .takeWhile(Functions.remainderInt(2))
                .iterator();
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator3() {
        final PrimitiveIterator.OfInt it = IntStream.of(2)
                .takeWhile(Functions.remainderInt(2))
                .iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.nextInt(), is(2));
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
        boolean match = IntStream.of(42, 44, 46, 0, 48)
                .takeWhile(new IntPredicate() {
                    @Override
                    public boolean test(int value) {
                        return value != 0;
                    }
                })
                .map(new IntUnaryOperator() {
                    @Override
                    public int applyAsInt(int value) {
                        return value + 2;
                    }
                })
                .anyMatch(Functions.remainderInt(10));
        assertFalse(match);
    }
}
