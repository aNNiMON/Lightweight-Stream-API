package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntSupplier;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class ToArrayTest {

    @Test
    public void testToArray() {
        assertEquals(IntStream.empty().toArray().length, 0);
        assertEquals(IntStream.of(100).toArray()[0], 100);
        assertEquals(IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).skip(4).toArray().length, 5);

        assertEquals(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return -1;
            }
        }).limit(14).toArray().length, 14);

        assertEquals(IntStream.of(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return -1;
            }
        }).limit(14).toArray()).sum(), -14);
    }
}
