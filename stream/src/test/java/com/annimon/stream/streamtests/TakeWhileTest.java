package com.annimon.stream.streamtests;

import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.UnaryOperator;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(2))
                .custom(assertElements(contains(2, 4, 6)));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(3))
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(1))
                .custom(assertElements(contains(2, 4, 6, 7, 8, 10, 11)));
    }

    @Test
    public void testTakeWhileIterator() {
        Iterator<? extends Integer> it =
                Stream.of(2, 4, 5, 6).takeWhile(Functions.remainder(2)).iterator();
        assertThat(it.next(), is(2));
        assertThat(it.next(), is(4));
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator2() {
        Iterator<? extends Integer> it = Stream.of(1).takeWhile(Functions.remainder(2)).iterator();
        assertThat(it.hasNext(), is(false));
        assertThat(it.hasNext(), is(false));
    }

    @Test
    public void testTakeWhileIterator3() {
        final Iterator<? extends Integer> it =
                Stream.of(2).takeWhile(Functions.remainder(2)).iterator();
        assertThat(it.hasNext(), is(true));
        assertThat(it.hasNext(), is(true));
        assertThat(it.next(), is(2));
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
                Stream.of(42, 44, 46, null, 48)
                        .takeWhile(
                                new Predicate<Integer>() {
                                    @Override
                                    public boolean test(Integer value) {
                                        return value != null;
                                    }
                                })
                        .map(
                                new UnaryOperator<Integer>() {
                                    @Override
                                    public Integer apply(Integer integer) {
                                        return integer + 2;
                                    }
                                })
                        .anyMatch(Functions.remainder(10));
        assertFalse(match);
    }
}
