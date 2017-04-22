package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OnCloseTest {

    @Test
    public void testOnClose() {
        final boolean[] state = new boolean[] { false };
        Stream<Integer> stream = Stream.of(0, 1, 2)
                .onClose(new Runnable() {
                    @Override
                    public void run() {
                        state[0] = true;
                    }
                });
        stream.findFirst();
        stream.close();
        assertTrue(state[0]);
    }

    @Test
    public void testOnCloseWithOtherOperators() {
        final boolean[] state = new boolean[] { false };
        Stream<Integer> stream = Stream.of(0, 1, 2, 2, 3, 4, 4, 5)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer value) {
                        return value < 4;
                    }
                })
                .onClose(new Runnable() {
                    @Override
                    public void run() {
                        state[0] = true;
                    }
                })
                .distinct()
                .limit(2);
        stream.findFirst();
        stream.close();
        assertTrue(state[0]);
    }

    @Test
    public void testOnCloseFlatMap() {
        final int[] counter = new int[] { 0 };
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
            }
        };

        Stream<Integer> stream = Stream.rangeClosed(2, 4)
                .onClose(runnable)
                .onClose(runnable)
                .flatMap(new Function<Integer, Stream<Integer>>() {
                    @Override
                    public Stream<Integer> apply(final Integer i) {
                        return Stream.rangeClosed(2, 4)
                                .onClose(runnable);
                    }
                });
        stream.count();
        assertThat(counter[0], is(3));
        stream.close();
        assertThat(counter[0], is(5));
    }

    @Test
    public void testOnCloseConcat() {
        final int[] counter = new int[] { 0 };
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
            }
        };

        Stream<Integer> stream1 = Stream.range(0, 2).onClose(runnable);
        Stream<Integer> stream2 = Stream.range(0, 2).onClose(runnable);
        Stream<Integer> stream = Stream.concat(stream1, stream2)
                .onClose(runnable);
        stream.count();
        assertThat(counter[0], is(0));
        stream.close();
        assertThat(counter[0], is(3));
    }
}
