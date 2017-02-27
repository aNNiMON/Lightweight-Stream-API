package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntSupplier;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class CountTest {

    @Test
    public void testCount() {
        assertEquals(IntStream.empty().count(), 0);
        assertEquals(IntStream.of(42).count(), 1);
        assertEquals(IntStream.range(1, 7).count(), 6);
        assertEquals(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return 1;
            }
        }).limit(10).count(), 10);

        assertEquals(IntStream.rangeClosed(1, 7).skip(3).count(), 4);
    }
}
