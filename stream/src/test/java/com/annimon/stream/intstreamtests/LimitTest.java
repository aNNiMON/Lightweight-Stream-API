package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntSupplier;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class LimitTest {

    @Test
    public void testLimit() {
        assertEquals(3, IntStream.of(1, 2, 3, 4, 5, 6).limit(3).count());
        assertEquals(6, IntStream.generate(new IntSupplier() {

            int current = 42;

            @Override
            public int getAsInt() {
                current = current + current << 1;
                return current;
            }
        }).limit(6).count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        IntStream.of(42).limit(-1).count();
    }

    @Test
    public void testLimitZero() {
        assertEquals(0, IntStream.of(1, 2).limit(0).count());
    }

    @Test
    public void testLimitMoreThanCount() {
        assertThat(IntStream.range(0, 5).limit(15).count(), is(5L));
    }
}
