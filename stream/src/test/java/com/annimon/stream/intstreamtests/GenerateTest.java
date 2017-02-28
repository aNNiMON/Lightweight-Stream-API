package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntSupplier;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class GenerateTest {

    @Test
    public void testStreamGenerate() {
        IntSupplier s = new IntSupplier() {
            @Override
            public int getAsInt() {
                return 42;
            }
        };

        assertTrue(IntStream.generate(s).findFirst().getAsInt() == 42);
    }

    @Test(expected = NullPointerException.class)
    public void testStreamGenerateNull() {
        IntStream.generate(null);
    }
}
