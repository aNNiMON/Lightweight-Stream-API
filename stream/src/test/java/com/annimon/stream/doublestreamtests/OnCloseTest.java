package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleFunction;
import com.annimon.stream.function.DoublePredicate;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OnCloseTest {

    @Test
    public void testOnClose() {
        final boolean[] state = new boolean[] { false };
        DoubleStream stream = DoubleStream.of(0, 1, 2)
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
        DoubleStream stream = DoubleStream.of(0, 1, 2, 2, 3, 4, 4, 5)
                .filter(new DoublePredicate() {
                    @Override
                    public boolean test(double value) {
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

        DoubleStream stream = DoubleStream.of(2, 3, 4)
                .onClose(runnable)
                .onClose(runnable)
                .flatMap(new DoubleFunction<DoubleStream>() {
                    @Override
                    public DoubleStream apply(final double i) {
                        return DoubleStream.of(2, 3, 4)
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

        DoubleStream stream1 = DoubleStream.of(0, 1).onClose(runnable);
        DoubleStream stream2 = DoubleStream.of(2, 3).onClose(runnable);
        DoubleStream stream = DoubleStream.concat(stream1, stream2)
                .onClose(runnable);
        stream.count();
        assertThat(counter[0], is(0));
        stream.close();
        assertThat(counter[0], is(3));
    }
}
