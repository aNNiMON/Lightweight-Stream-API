package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.IndexedPredicate;
import com.annimon.stream.function.UnaryOperator;
import com.annimon.stream.internal.SpinedBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public final class TakeWhileIndexedTest {

    @Test
    public void testTakeWhileIndexed() {
        Stream.of(1, 2, 3,  4, -5, -6, -7)
                .takeWhileIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return index + value < 5;
                    }
                })
                .custom(assertElements(contains(
                        1, 2
                )));
    }

    @Test
    public void testTakeWhileIndexedWithStartAndStep() {
        Stream.of(1, 2, 3,  4, -5, -6, -7)
                .takeWhileIndexed(2, 2, new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return index + value < 8;
                    }
                })
                .custom(assertElements(contains(
                        1, 2
                )));
    }

    @Test
    public void testIssue186OnIndexed() {
        AtomicInteger result = Stream.iterate(
                new AtomicInteger(0),
                UnaryOperator.Util.<AtomicInteger>identity())
                .takeWhileIndexed(new IndexedPredicate<AtomicInteger>() {
                    @Override
                    public boolean test(int index, AtomicInteger value) {
                        return value.incrementAndGet() < 3;
                    }
                })
                .findFirst()
                .orElseThrow();
        assertEquals(1, result.get());
    }

    @Test
    public void testTakeWhileIterator() {
        int[] index = {0, 1, 2};
        int[] input = {2, 3, 5, 5};
        int[] sum   = {2, 4, 7};
        final SpinedBuffer.OfInt indexBuffer = new SpinedBuffer.OfInt();
        final SpinedBuffer.OfInt sumBuffer = new SpinedBuffer.OfInt();
        Iterator<? extends Integer> it = IntStream.of(input)
                .boxed()
                .takeWhileIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        indexBuffer.accept(index);
                        sumBuffer.accept(index + value);
                        return (index + value) % 2 == 0;
                    }
                })
                .iterator();
        assertThat(it.next(), is(2));
        assertThat(it.next(), is(3));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
        assertThat(indexBuffer.asPrimitiveArray(), is(index));
        assertThat(sumBuffer.asPrimitiveArray(), is(sum));
    }

    @Test
    public void testTakeWhileIterator2() {
        Iterator<? extends Integer> it = Stream.of(1)
                .takeWhileIndexed(sumRemainder(2))
                .iterator();
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator3() {
        final Iterator<? extends Integer> it = Stream.of(2)
                .takeWhileIndexed(sumRemainder(2))
                .iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
        assertThat(it.hasNext(), is(false));
        assertThrows(NoSuchElementException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                it.next();
            }
        });
    }

    @Test
    public void testTakeWhileIndexedAndMapIssue193() {
        boolean match = Stream.of(42, 44, 46, null, 48)
                .takeWhileIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return value != null;
                    }
                })
                .map(new UnaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer integer) {
                        return integer + 2;
                    }
                })
                .anyMatch(Functions.remainder(10));
        assertFalse(match);
    }

    private IndexedPredicate<Integer> sumRemainder(final int x) {
        return new IndexedPredicate<Integer>() {
            @Override
            public boolean test(int index, Integer value) {
                return (index + value) % x == 0;
            }
        };
    }
}
