package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongFunction;
import com.annimon.stream.function.LongPredicate;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OnCloseTest {

    @Test
    public void testOnClose() {
        final boolean[] state = new boolean[] { false };
        LongStream stream = LongStream.of(0, 1, 2)
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
        LongStream stream = LongStream.of(0, 1, 2, 2, 3, 4, 4, 5)
                .filter(new LongPredicate() {
                    @Override
                    public boolean test(long value) {
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

        LongStream stream = LongStream.rangeClosed(2, 4)
                .onClose(runnable)
                .onClose(runnable)
                .flatMap(new LongFunction<LongStream>() {
                    @Override
                    public LongStream apply(final long i) {
                        return LongStream.rangeClosed(2, 4)
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

        LongStream stream1 = LongStream.range(0, 2).onClose(runnable);
        LongStream stream2 = LongStream.range(0, 2).onClose(runnable);
        LongStream stream = LongStream.concat(stream1, stream2)
                .onClose(runnable);
        stream.count();
        assertThat(counter[0], is(0));
        stream.close();
        assertThat(counter[0], is(3));
    }
}
