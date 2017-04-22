package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntFunction;
import com.annimon.stream.function.IntPredicate;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OnCloseTest {

    @Test
    public void testOnClose() {
        final boolean[] state = new boolean[] { false };
        IntStream stream = IntStream.of(0, 1, 2)
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
        IntStream stream = IntStream.of(0, 1, 2, 2, 3, 4, 4, 5)
                .filter(new IntPredicate() {
                    @Override
                    public boolean test(int value) {
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

        IntStream stream = IntStream.rangeClosed(2, 4)
                .onClose(runnable)
                .onClose(runnable)
                .flatMap(new IntFunction<IntStream>() {
                    @Override
                    public IntStream apply(final int i) {
                        return IntStream.rangeClosed(2, 4)
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

        IntStream stream1 = IntStream.range(0, 2).onClose(runnable);
        IntStream stream2 = IntStream.range(0, 2).onClose(runnable);
        IntStream stream = IntStream.concat(stream1, stream2)
                .onClose(runnable);
        stream.count();
        assertThat(counter[0], is(0));
        stream.close();
        assertThat(counter[0], is(3));
    }
}
